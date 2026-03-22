package com.xpf.p2p.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.xpf.p2p.R

/**
 * Created by Administrator on 2016/10/31.
 */
class LockIndicator : View {

    private val numRow = 3
    private val numColum = 3
    private var patternWidth = 40
    private var patternHeight = 40
    private var f = 5
    private var g = 5
    private val strokeWidth = 3
    private var paint: Paint? = null
    private var patternNoraml: android.graphics.drawable.Drawable? = null
    private var patternPressed: android.graphics.drawable.Drawable? = null
    private var lockPassStr: String? = null

    constructor(paramContext: Context) : super(paramContext)

    constructor(paramContext: Context, paramAttributeSet: AttributeSet) : super(paramContext, paramAttributeSet, 0) {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = strokeWidth.toFloat()
        paint!!.style = Paint.Style.STROKE
        @Suppress("DEPRECATION")
        patternNoraml = resources.getDrawable(R.drawable.lock_pattern_node_normal)
        @Suppress("DEPRECATION")
        patternPressed = resources.getDrawable(R.drawable.lock_pattern_node_pressed)
        if (patternPressed != null) {
            patternWidth = patternPressed!!.intrinsicWidth
            patternHeight = patternPressed!!.intrinsicHeight
            f = patternWidth / 4
            g = patternHeight / 4
            patternPressed!!.setBounds(0, 0, patternWidth, patternHeight)
            patternNoraml!!.setBounds(0, 0, patternWidth, patternHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (patternPressed == null || patternNoraml == null) {
            return
        }
        for (i in 0 until numRow) {
            for (j in 0 until numColum) {
                paint!!.color = -16777216
                val i1 = j * patternHeight + j * g
                val i2 = i * patternWidth + i * f
                canvas.save()
                canvas.translate(i1.toFloat(), i2.toFloat())
                val curNum = (numColum * i + (j + 1)).toString()
                if (!TextUtils.isEmpty(lockPassStr)) {
                    if (lockPassStr!!.indexOf(curNum) == -1) {
                        patternNoraml!!.draw(canvas)
                    } else {
                        patternPressed!!.draw(canvas)
                    }
                } else {
                    patternNoraml!!.draw(canvas)
                }
                canvas.restore()
            }
        }
    }

    override fun onMeasure(paramInt1: Int, paramInt2: Int) {
        if (patternPressed != null) {
            setMeasuredDimension(
                numColum * patternHeight + g * (-1 + numColum),
                numRow * patternWidth + f * (-1 + numRow)
            )
        }
    }

    fun setPath(paramString: String?) {
        lockPassStr = paramString
        invalidate()
    }
}
