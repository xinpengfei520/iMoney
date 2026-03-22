package com.xpf.p2p.widget

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.xpf.p2p.R
import com.xpf.p2p.entity.GesturePoint
import com.xpf.p2p.utils.AppUtil

/**
 * Created by Administrator on 2016/10/31.
 */
class GestureContentView(
    context: Context,
    isVerify: Boolean,
    passWord: String,
    callBack: GestureDrawline.GestureCallBack
) : ViewGroup(context) {

    private val baseNum = 6
    private val screenDispaly: IntArray = AppUtil.getScreenDisplay(context)
    private val blockWidth: Int = screenDispaly[0] / 3
    private val list: MutableList<GesturePoint> = ArrayList()
    private val gestureDrawline: GestureDrawline

    init {
        addChild(context)
        gestureDrawline = GestureDrawline(context, list, isVerify, passWord, callBack)
    }

    private fun addChild(context: Context) {
        for (i in 0 until 9) {
            val image = ImageView(context)
            image.setBackgroundResource(R.drawable.gesture_node_normal)
            this.addView(image)
            invalidate()

            val row = i / 3
            val col = i % 3
            val leftX = col * blockWidth + blockWidth / baseNum
            val topY = row * blockWidth + blockWidth / baseNum
            val rightX = col * blockWidth + blockWidth - blockWidth / baseNum
            val bottomY = row * blockWidth + blockWidth - blockWidth / baseNum
            val p = GesturePoint(leftX, rightX, topY, bottomY, image, i + 1)
            list.add(p)
        }
    }

    fun setParentView(parent: ViewGroup) {
        val width = screenDispaly[0]
        val layoutParams = LayoutParams(width, width)
        this.layoutParams = layoutParams
        gestureDrawline.layoutParams = layoutParams
        parent.addView(gestureDrawline)
        parent.addView(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val row = i / 3
            val col = i % 3
            val v = getChildAt(i)
            v.layout(
                col * blockWidth + blockWidth / baseNum,
                row * blockWidth + blockWidth / baseNum,
                col * blockWidth + blockWidth - blockWidth / baseNum,
                row * blockWidth + blockWidth - blockWidth / baseNum
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            v.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun clearDrawlineState(delayTime: Long) {
        gestureDrawline.clearDrawlineState(delayTime)
    }
}
