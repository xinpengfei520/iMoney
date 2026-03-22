package com.xpf.p2p.widget.randomLayout

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation

object AnimationUtil {

    private const val MEDIUM = 500L

    @JvmStatic
    fun createZoomInNearAnim(): Animation {
        val ret = AnimationSet(false)
        var anim: Animation = AlphaAnimation(0f, 1f)
        anim.duration = MEDIUM
        anim.interpolator = LinearInterpolator()
        ret.addAnimation(anim)
        anim = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        return ret
    }

    @JvmStatic
    fun createZoomInAwayAnim(): Animation {
        val ret = AnimationSet(false)
        var anim: Animation = AlphaAnimation(1f, 0f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        anim = ScaleAnimation(1f, 3f, 1f, 3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        return ret
    }

    @JvmStatic
    fun createZoomOutNearAnim(): Animation {
        val ret = AnimationSet(false)
        var anim: Animation = AlphaAnimation(0f, 1f)
        anim.duration = MEDIUM
        anim.interpolator = LinearInterpolator()
        ret.addAnimation(anim)
        anim = ScaleAnimation(3f, 1f, 3f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        return ret
    }

    @JvmStatic
    fun createZoomOutAwayAnim(): Animation {
        val ret = AnimationSet(false)
        var anim: Animation = AlphaAnimation(1f, 0f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        anim = ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        return ret
    }

    @JvmStatic
    fun createPanInAnim(degree: Float): Animation {
        val ret = AnimationSet(false)
        var anim: Animation = AlphaAnimation(0f, 1f)
        anim.duration = MEDIUM
        anim.interpolator = LinearInterpolator()
        ret.addAnimation(anim)
        val pivotX = (1 - Math.cos(degree.toDouble())).toFloat() / 2
        val pivotY = (1 + Math.sin(degree.toDouble())).toFloat() / 2
        anim = ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        return ret
    }

    @JvmStatic
    fun createPanOutAnim(degree: Float): Animation {
        val ret = AnimationSet(false)
        var anim: Animation = AlphaAnimation(1f, 0f)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        val pivotX = (1 + Math.cos(degree.toDouble())).toFloat() / 2
        val pivotY = (1 - Math.sin(degree.toDouble())).toFloat() / 2
        anim = ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY)
        anim.duration = MEDIUM
        anim.interpolator = DecelerateInterpolator()
        ret.addAnimation(anim)
        return ret
    }
}
