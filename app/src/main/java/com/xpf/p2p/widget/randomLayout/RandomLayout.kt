package com.xpf.p2p.widget.randomLayout

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import java.util.LinkedList
import java.util.Random

class RandomLayout(context: Context) : ViewGroup(context) {

    private var mRdm: Random
    private var mXRegularity: Int = 0
    private var mYRegularity: Int = 0
    private var mAreaCount: Int = 0
    private lateinit var mAreaDensity: Array<IntArray>
    private var mFixedViews: MutableSet<View>
    private var mAdapter: Adapter? = null
    private var mRecycledViews: MutableList<View>
    private var mLayouted: Boolean = false
    private val mOverlapAdd = 2

    init {
        mRdm = Random()
        setRegularity(1, 1)
        mFixedViews = HashSet()
        mRecycledViews = LinkedList()
    }

    fun hasLayouted(): Boolean = mLayouted

    fun setRegularity(xRegularity: Int, yRegularity: Int) {
        mXRegularity = if (xRegularity > 1) xRegularity else 1
        mYRegularity = if (yRegularity > 1) yRegularity else 1
        mAreaCount = mXRegularity * mYRegularity
        mAreaDensity = Array(mYRegularity) { IntArray(mXRegularity) }
    }

    fun setAdapter(adapter: Adapter?) {
        mAdapter = adapter
    }

    private fun resetAllAreas() {
        mFixedViews.clear()
        for (i in 0 until mYRegularity) {
            for (j in 0 until mXRegularity) {
                mAreaDensity[i][j] = 0
            }
        }
    }

    private fun pushRecycler(scrapView: View?) {
        if (scrapView != null) {
            mRecycledViews.add(0, scrapView)
        }
    }

    private fun popRecycler(): View? {
        return if (mRecycledViews.size > 0) mRecycledViews.removeAt(0) else null
    }

    private fun generateChildren() {
        if (mAdapter == null) return
        val childCount = super.getChildCount()
        for (i in childCount - 1 downTo 0) {
            pushRecycler(super.getChildAt(i))
        }
        super.removeAllViewsInLayout()
        val count = mAdapter!!.getCount()
        for (i in 0 until count) {
            val convertView = popRecycler()
            val newChild = mAdapter!!.getView(i, convertView)
            if (newChild !== convertView) {
                pushRecycler(convertView)
            }
            super.addView(newChild, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }

    fun redistribute() {
        resetAllAreas()
        requestLayout()
    }

    fun refresh() {
        resetAllAreas()
        generateChildren()
        requestLayout()
    }

    override fun removeAllViews() {
        super.removeAllViews()
        resetAllAreas()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val thisW = r - l - this.paddingLeft - this.paddingRight
        val thisH = b - t - this.paddingTop - this.paddingBottom
        val contentRight = r - paddingRight
        val contentBottom = b - paddingBottom

        val availAreas = ArrayList<Int>(mAreaCount)
        for (i in 0 until mAreaCount) {
            availAreas.add(i)
        }

        val areaCapacity = (count + 1) / mAreaCount + 1
        var availAreaCount = mAreaCount

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue

            if (!mFixedViews.contains(child)) {
                val params = child.layoutParams as LayoutParams
                val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.measuredWidth, MeasureSpec.AT_MOST)
                val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(this.measuredHeight, MeasureSpec.AT_MOST)
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)

                val childW = child.measuredWidth
                val childH = child.measuredHeight
                val colW = thisW / mXRegularity.toFloat()
                val rowH = thisH / mYRegularity.toFloat()

                while (availAreaCount > 0) {
                    val arrayIdx = mRdm.nextInt(availAreaCount)
                    val areaIdx = availAreas[arrayIdx]
                    val col = areaIdx % mXRegularity
                    val row = areaIdx / mXRegularity
                    if (mAreaDensity[row][col] < areaCapacity) {
                        var xOffset = colW.toInt() - childW
                        if (xOffset <= 0) xOffset = 1
                        var yOffset = rowH.toInt() - childH
                        if (yOffset <= 0) yOffset = 1

                        params.mLeft = paddingLeft + (colW * col + mRdm.nextInt(xOffset)).toInt()
                        val rightEdge = contentRight - childW
                        if (params.mLeft > rightEdge) params.mLeft = rightEdge
                        params.mRight = params.mLeft + childW

                        params.mTop = paddingTop + (rowH * row + mRdm.nextInt(yOffset)).toInt()
                        val bottomEdge = contentBottom - childH
                        if (params.mTop > bottomEdge) params.mTop = bottomEdge
                        params.mBottom = params.mTop + childH

                        if (!isOverlap(params)) {
                            mAreaDensity[row][col]++
                            child.layout(params.mLeft, params.mTop, params.mRight, params.mBottom)
                            mFixedViews.add(child)
                            break
                        } else {
                            availAreas.removeAt(arrayIdx)
                            availAreaCount--
                        }
                    } else {
                        availAreas.removeAt(arrayIdx)
                        availAreaCount--
                    }
                }
            }
        }
        mLayouted = true
    }

    private fun isOverlap(params: LayoutParams): Boolean {
        val l = params.mLeft - mOverlapAdd
        val t = params.mTop - mOverlapAdd
        val r = params.mRight + mOverlapAdd
        val b = params.mBottom + mOverlapAdd

        val rect = Rect()
        for (v in mFixedViews) {
            val vl = v.left - mOverlapAdd
            val vt = v.top - mOverlapAdd
            val vr = v.right + mOverlapAdd
            val vb = v.bottom + mOverlapAdd
            rect.left = maxOf(l, vl)
            rect.top = maxOf(t, vt)
            rect.right = minOf(r, vr)
            rect.bottom = minOf(b, vb)
            if (rect.right >= rect.left && rect.bottom >= rect.top) {
                return true
            }
        }
        return false
    }

    interface Adapter {
        fun getCount(): Int
        fun getView(position: Int, convertView: View?): View
    }

    class LayoutParams : ViewGroup.LayoutParams {
        var mLeft: Int = 0
        var mRight: Int = 0
        var mTop: Int = 0
        var mBottom: Int = 0

        constructor(source: ViewGroup.LayoutParams) : super(source)
        constructor(w: Int, h: Int) : super(w, h)
    }
}
