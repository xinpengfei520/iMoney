package com.xpf.p2p.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ScrollView
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/14 :)
 * Wechat:vancexin
 * Function:自定义带有回弹效果的ScrollView
 */
class MyScrollView : ScrollView {

    private var childView: View? = null
    private var lastX = 0
    private var lastY = 0
    private var downX = 0
    private var downY = 0
    private val normal = Rect()
    private var isFinishAnimation = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            childView = getChildAt(0)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var isIntercept = false
        val eventX = ev.x.toInt()
        val eventY = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = eventX
                lastX = downX
                downY = eventY
                lastY = downY
            }
            MotionEvent.ACTION_MOVE -> {
                val totalX = Math.abs(eventX - downX)
                val totalY = Math.abs(eventY - downY)
                if (totalX < totalY && totalY > UIUtils.dp2px(10)) {
                    isIntercept = true
                }
                lastX = eventX
                lastY = eventY
            }
        }
        return isIntercept
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (childView == null || !isFinishAnimation) {
            return super.onTouchEvent(ev)
        }

        val eventY = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = eventY
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = eventY - lastY
                if (isNeedMove()) {
                    if (normal.isEmpty) {
                        normal.set(childView!!.left, childView!!.top, childView!!.right, childView!!.bottom)
                    }
                    childView!!.layout(
                        childView!!.left, childView!!.top + dy / 2,
                        childView!!.right, childView!!.bottom + dy / 2
                    )
                }
                lastY = eventY
            }
            MotionEvent.ACTION_UP -> {
                if (isNeedAnimation()) {
                    val animation = TranslateAnimation(0f, 0f, 0f, (normal.bottom - childView!!.bottom).toFloat())
                    animation.duration = 200
                    childView!!.startAnimation(animation)

                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            isFinishAnimation = false
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            isFinishAnimation = true
                            childView!!.clearAnimation()
                            childView!!.layout(normal.left, normal.top, normal.right, normal.bottom)
                            normal.setEmpty()
                        }

                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun isNeedAnimation(): Boolean {
        return !normal.isEmpty
    }

    private fun isNeedMove(): Boolean {
        val measureHeight = childView!!.measuredHeight
        val height = this.height
        Log.e("TAG", "measureHeight$measureHeight,height = $height")
        val dy = measureHeight - height
        val scrollY = this.scrollY
        return scrollY <= 0 || scrollY >= dy
    }
}
