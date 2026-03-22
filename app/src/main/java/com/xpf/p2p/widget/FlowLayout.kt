package com.xpf.p2p.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * Created by xpf on 2016/11/16 :)
 * Wechat:vancexin
 * Function:自定义流式布局效果
 */
class FlowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val allHeights = ArrayList<Int>()
    private val allViews = ArrayList<List<View>>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = 0
        var lineWidth = 0
        var lineHeight = 0

        val childCount = childCount
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight
            val mp = childView.layoutParams as MarginLayoutParams
            if (lineWidth + childWidth + mp.leftMargin + mp.rightMargin <= widthSize) {
                lineWidth += childWidth + mp.leftMargin + mp.rightMargin
                lineHeight = maxOf(lineHeight, childHeight + mp.topMargin + mp.bottomMargin)
            } else {
                width = maxOf(width, lineWidth)
                height += lineHeight
                lineWidth = childWidth + mp.leftMargin + mp.rightMargin
                lineHeight = childHeight + mp.topMargin + mp.bottomMargin
            }
            if (i == childCount - 1) {
                width = maxOf(width, lineWidth)
                height += lineHeight
            }
        }
        Log.e(TAG, "width = $width,height = $height")
        Log.e(TAG, "widthSize = $widthSize,heightSize = $heightSize")

        setMeasuredDimension(
            if (widthMode == MeasureSpec.EXACTLY) widthSize else width,
            if (heightMode == MeasureSpec.EXACTLY) heightSize else height
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = this.width
        var lineWidth = 0
        var lineHeight = 0

        val childCount = childCount
        var lineList = ArrayList<View>()

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight
            val mp = childView.layoutParams as MarginLayoutParams
            if (lineWidth + childWidth + mp.leftMargin + mp.rightMargin <= width) {
                lineList.add(childView)
                lineWidth += childWidth + mp.leftMargin + mp.rightMargin
                lineHeight = maxOf(lineHeight, childHeight + mp.topMargin + mp.bottomMargin)
            } else {
                allViews.add(lineList)
                allHeights.add(lineHeight)
                lineList = ArrayList()
                lineList.add(childView)
                lineWidth = childWidth + mp.leftMargin + mp.rightMargin
                lineHeight = childHeight + mp.topMargin + mp.bottomMargin
            }
            if (i == childCount - 1) {
                allViews.add(lineList)
                allHeights.add(lineHeight)
            }
        }
        Log.e(TAG, "allViews.size = ${allViews.size},allHeights.size = ${allHeights.size}")

        var x = 0
        var y = 0

        for (i in allViews.indices) {
            val lineViews = allViews[i]
            for (j in lineViews.indices) {
                val childView = lineViews[j]
                val mp = childView.layoutParams as MarginLayoutParams
                val left = x + mp.leftMargin
                val top = y + mp.topMargin
                val right = left + childView.measuredWidth
                val bottom = top + childView.measuredHeight
                childView.layout(left, top, right, bottom)
                x += childView.measuredWidth + mp.leftMargin + mp.rightMargin
            }
            x = 0
            y += allHeights[i]
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    companion object {
        private const val TAG = "FlowLayout"
    }
}
