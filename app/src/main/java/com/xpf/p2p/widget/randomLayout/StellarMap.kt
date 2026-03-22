package com.xpf.p2p.widget.randomLayout

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout

class StellarMap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), Animation.AnimationListener, View.OnTouchListener, GestureDetector.OnGestureListener {

    private var mHidenGroup: RandomLayout
    private var mShownGroup: RandomLayout
    private var mAdapter: Adapter? = null
    private var mShownGroupAdapter: RandomLayout.Adapter? = null
    private var mHidenGroupAdapter: RandomLayout.Adapter? = null
    private var mShownGroupIndex: Int = -1
    private var mHidenGroupIndex: Int = -1
    private var mGroupCount: Int = 0

    private val mZoomInNearAnim: Animation
    private val mZoomInAwayAnim: Animation
    private val mZoomOutNearAnim: Animation
    private val mZoomOutAwayAnim: Animation
    private var mPanInAnim: Animation? = null
    private var mPanOutAnim: Animation? = null

    private val mGestureDetector: GestureDetector

    init {
        mHidenGroup = RandomLayout(getContext())
        mShownGroup = RandomLayout(getContext())

        @Suppress("DEPRECATION")
        addView(mHidenGroup, LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT))
        mHidenGroup.visibility = View.GONE
        @Suppress("DEPRECATION")
        addView(mShownGroup, LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT))

        @Suppress("DEPRECATION")
        mGestureDetector = GestureDetector(this)
        setOnTouchListener(this)

        mZoomInNearAnim = AnimationUtil.createZoomInNearAnim()
        mZoomInNearAnim.setAnimationListener(this)
        mZoomInAwayAnim = AnimationUtil.createZoomInAwayAnim()
        mZoomInAwayAnim.setAnimationListener(this)
        mZoomOutNearAnim = AnimationUtil.createZoomOutNearAnim()
        mZoomOutNearAnim.setAnimationListener(this)
        mZoomOutAwayAnim = AnimationUtil.createZoomOutAwayAnim()
        mZoomOutAwayAnim.setAnimationListener(this)
    }

    fun setRegularity(xRegularity: Int, yRegularity: Int) {
        mHidenGroup.setRegularity(xRegularity, yRegularity)
        mShownGroup.setRegularity(xRegularity, yRegularity)
    }

    private fun setChildAdapter() {
        if (mAdapter == null) return
        mHidenGroupAdapter = object : RandomLayout.Adapter {
            override fun getView(position: Int, convertView: View?): View {
                return mAdapter!!.getView(mHidenGroupIndex, position, convertView)
            }

            override fun getCount(): Int {
                return mAdapter!!.getCount(mHidenGroupIndex)
            }
        }
        mHidenGroup.setAdapter(mHidenGroupAdapter)

        mShownGroupAdapter = object : RandomLayout.Adapter {
            override fun getView(position: Int, convertView: View?): View {
                return mAdapter!!.getView(mShownGroupIndex, position, convertView)
            }

            override fun getCount(): Int {
                return mAdapter!!.getCount(mShownGroupIndex)
            }
        }
        mShownGroup.setAdapter(mShownGroupAdapter)
    }

    fun setAdapter(adapter: Adapter) {
        mAdapter = adapter
        mGroupCount = mAdapter!!.getGroupCount()
        if (mGroupCount > 0) {
            mShownGroupIndex = 0
        }
        setChildAdapter()
    }

    fun setInnerPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        mHidenGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        mShownGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    fun setGroup(groupIndex: Int, playAnimation: Boolean) {
        switchGroup(groupIndex, playAnimation, mZoomInNearAnim, mZoomInAwayAnim)
    }

    fun getCurrentGroup(): Int = mShownGroupIndex

    fun zoomIn() {
        val nextGroupIndex = mAdapter!!.getNextGroupOnZoom(mShownGroupIndex, true)
        switchGroup(nextGroupIndex, true, mZoomInNearAnim, mZoomInAwayAnim)
    }

    fun zoomOut() {
        val nextGroupIndex = mAdapter!!.getNextGroupOnZoom(mShownGroupIndex, false)
        switchGroup(nextGroupIndex, true, mZoomOutNearAnim, mZoomOutAwayAnim)
    }

    fun pan(degree: Float) {
        val nextGroupIndex = mAdapter!!.getNextGroupOnPan(mShownGroupIndex, degree)
        mPanInAnim = AnimationUtil.createPanInAnim(degree)
        mPanInAnim!!.setAnimationListener(this)
        mPanOutAnim = AnimationUtil.createPanOutAnim(degree)
        mPanOutAnim!!.setAnimationListener(this)
        switchGroup(nextGroupIndex, true, mPanInAnim!!, mPanOutAnim!!)
    }

    private fun switchGroup(newGroupIndex: Int, playAnimation: Boolean, inAnim: Animation, outAnim: Animation) {
        if (newGroupIndex < 0 || newGroupIndex >= mGroupCount) return

        mHidenGroupIndex = mShownGroupIndex
        mShownGroupIndex = newGroupIndex

        val temp = mShownGroup
        mShownGroup = mHidenGroup
        mShownGroup.setAdapter(mShownGroupAdapter)
        mHidenGroup = temp
        mHidenGroup.setAdapter(mHidenGroupAdapter)

        mShownGroup.refresh()
        mShownGroup.visibility = View.VISIBLE

        if (playAnimation) {
            if (mShownGroup.hasLayouted()) {
                mShownGroup.startAnimation(inAnim)
            }
            mHidenGroup.startAnimation(outAnim)
        } else {
            mHidenGroup.visibility = View.GONE
        }
    }

    fun redistribute() {
        mShownGroup.redistribute()
    }

    override fun onAnimationStart(animation: Animation) {}

    override fun onAnimationEnd(animation: Animation) {
        if (animation === mZoomInAwayAnim || animation === mZoomOutAwayAnim || animation === mPanOutAnim) {
            mHidenGroup.visibility = View.GONE
        }
    }

    override fun onAnimationRepeat(animation: Animation) {}

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val hasLayoutedBefore = mShownGroup.hasLayouted()
        super.onLayout(changed, l, t, r, b)
        if (!hasLayoutedBefore && mShownGroup.hasLayouted()) {
            mShownGroup.startAnimation(mZoomInNearAnim)
        } else {
            mShownGroup.visibility = View.VISIBLE
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return mGestureDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean = false

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean = false

    override fun onLongPress(e: MotionEvent) {}

    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val centerX = measuredWidth / 2
        val centerY = measuredWidth / 2
        val x1 = (e1?.x?.toInt() ?: 0) - centerX
        val y1 = (e1?.y?.toInt() ?: 0) - centerY
        val x2 = e2.x.toInt() - centerX
        val y2 = e2.y.toInt() - centerY

        if (x1 * x1 + y1 * y1 > x2 * x2 + y2 * y2) {
            zoomOut()
        } else {
            zoomIn()
        }
        return true
    }

    interface Adapter {
        fun getGroupCount(): Int
        fun getCount(group: Int): Int
        fun getView(group: Int, position: Int, convertView: View?): View
        fun getNextGroupOnPan(group: Int, degree: Float): Int
        fun getNextGroupOnZoom(group: Int, isZoomIn: Boolean): Int
    }
}
