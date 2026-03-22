package com.xpf.p2p.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Handler
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import com.xpf.p2p.constants.Constants
import com.xpf.p2p.entity.GesturePoint
import com.xpf.p2p.utils.AppUtil

/**
 * Created by Administrator on 2016/10/31.
 */
class GestureDrawline(
    context: Context,
    private val list: List<GesturePoint>,
    private val isVerify: Boolean,
    private val passWord: String,
    private val callBack: GestureCallBack
) : View(context) {

    private var mov_x: Int = 0
    private var mov_y: Int = 0
    private val paint: Paint
    private val canvas: Canvas
    private val bitmap: Bitmap
    private val lineList: MutableList<Pair<GesturePoint, GesturePoint>>
    private val autoCheckPointMap: MutableMap<String, GesturePoint?>
    private var isDrawEnable = true
    private val screenDispaly: IntArray = AppUtil.getScreenDisplay(context)
    private var currentPoint: GesturePoint? = null
    private var passWordSb: StringBuilder

    init {
        paint = Paint(Paint.DITHER_FLAG)
        bitmap = Bitmap.createBitmap(screenDispaly[0], screenDispaly[0], Bitmap.Config.ARGB_8888)
        canvas = Canvas()
        canvas.setBitmap(bitmap)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        paint.color = Color.rgb(245, 142, 33)
        paint.isAntiAlias = true

        lineList = ArrayList()
        autoCheckPointMap = HashMap()
        initAutoCheckPointMap()
        passWordSb = StringBuilder()
    }

    private fun initAutoCheckPointMap() {
        autoCheckPointMap["1,3"] = getGesturePointByNum(2)
        autoCheckPointMap["1,7"] = getGesturePointByNum(4)
        autoCheckPointMap["1,9"] = getGesturePointByNum(5)
        autoCheckPointMap["2,8"] = getGesturePointByNum(5)
        autoCheckPointMap["3,7"] = getGesturePointByNum(5)
        autoCheckPointMap["3,9"] = getGesturePointByNum(6)
        autoCheckPointMap["4,6"] = getGesturePointByNum(5)
        autoCheckPointMap["7,9"] = getGesturePointByNum(8)
    }

    private fun getGesturePointByNum(num: Int): GesturePoint? {
        for (point in list) {
            if (point.num == num) {
                return point
            }
        }
        return null
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isDrawEnable) {
            return true
        }
        paint.color = Color.rgb(245, 142, 33)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mov_x = event.x.toInt()
                mov_y = event.y.toInt()
                currentPoint = getPointAt(mov_x, mov_y)
                if (currentPoint != null) {
                    currentPoint!!.pointState = Constants.POINT_STATE_SELECTED
                    passWordSb.append(currentPoint!!.num)
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                clearScreenAndDrawList()
                val pointAt = getPointAt(event.x.toInt(), event.y.toInt())
                if (currentPoint == null && pointAt == null) {
                    return true
                } else {
                    if (currentPoint == null) {
                        currentPoint = pointAt
                        currentPoint!!.pointState = Constants.POINT_STATE_SELECTED
                        passWordSb.append(currentPoint!!.num)
                    }
                }
                if (pointAt == null || currentPoint == pointAt || Constants.POINT_STATE_SELECTED == pointAt.pointState) {
                    canvas.drawLine(
                        currentPoint!!.centerX.toFloat(), currentPoint!!.centerY.toFloat(),
                        event.x, event.y, paint
                    )
                } else {
                    canvas.drawLine(
                        currentPoint!!.centerX.toFloat(), currentPoint!!.centerY.toFloat(),
                        pointAt.centerX.toFloat(), pointAt.centerY.toFloat(), paint
                    )
                    pointAt.pointState = Constants.POINT_STATE_SELECTED

                    val betweenPoint = getBetweenCheckPoint(currentPoint!!, pointAt)
                    if (betweenPoint != null && Constants.POINT_STATE_SELECTED != betweenPoint.pointState) {
                        val pair1 = Pair(currentPoint!!, betweenPoint)
                        lineList.add(pair1)
                        passWordSb.append(betweenPoint.num)
                        val pair2 = Pair(betweenPoint, pointAt)
                        lineList.add(pair2)
                        passWordSb.append(pointAt.num)
                        betweenPoint.pointState = Constants.POINT_STATE_SELECTED
                        currentPoint = pointAt
                    } else {
                        val pair = Pair(currentPoint!!, pointAt)
                        lineList.add(pair)
                        passWordSb.append(pointAt.num)
                        currentPoint = pointAt
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (isVerify) {
                    if (passWord == passWordSb.toString()) {
                        callBack.checkedSuccess()
                    } else {
                        callBack.checkedFail()
                    }
                } else {
                    callBack.onGestureCodeInput(passWordSb.toString())
                }
            }
        }
        return true
    }

    fun clearDrawlineState(delayTime: Long) {
        if (delayTime > 0) {
            isDrawEnable = false
            drawErrorPathTip()
        }
        Handler().postDelayed(ClearStateRunnable(), delayTime)
    }

    internal inner class ClearStateRunnable : Runnable {
        override fun run() {
            passWordSb = StringBuilder()
            lineList.clear()
            clearScreenAndDrawList()
            for (p in list) {
                p.pointState = Constants.POINT_STATE_NORMAL
            }
            invalidate()
            isDrawEnable = true
        }
    }

    private fun getPointAt(x: Int, y: Int): GesturePoint? {
        for (point in list) {
            val leftX = point.leftX
            val rightX = point.rightX
            if (!(x >= leftX && x < rightX)) {
                continue
            }
            val topY = point.topY
            val bottomY = point.bottomY
            if (!(y >= topY && y < bottomY)) {
                continue
            }
            return point
        }
        return null
    }

    private fun getBetweenCheckPoint(pointStart: GesturePoint, pointEnd: GesturePoint): GesturePoint? {
        val startNum = pointStart.num
        val endNum = pointEnd.num
        val key = if (startNum < endNum) "$startNum,$endNum" else "$endNum,$startNum"
        return autoCheckPointMap[key]
    }

    private fun clearScreenAndDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        for (pair in lineList) {
            canvas.drawLine(
                pair.first.centerX.toFloat(), pair.first.centerY.toFloat(),
                pair.second.centerX.toFloat(), pair.second.centerY.toFloat(), paint
            )
        }
    }

    private fun drawErrorPathTip() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        paint.color = Color.rgb(154, 7, 21)
        for (pair in lineList) {
            pair.first.pointState = Constants.POINT_STATE_WRONG
            pair.second.pointState = Constants.POINT_STATE_WRONG
            canvas.drawLine(
                pair.first.centerX.toFloat(), pair.first.centerY.toFloat(),
                pair.second.centerX.toFloat(), pair.second.centerY.toFloat(), paint
            )
        }
        invalidate()
    }

    interface GestureCallBack {
        fun onGestureCodeInput(inputCode: String)
        fun checkedSuccess()
        fun checkedFail()
    }
}
