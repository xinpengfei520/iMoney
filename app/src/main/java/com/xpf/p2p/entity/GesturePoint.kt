package com.xpf.p2p.entity

import android.widget.ImageView
import com.xpf.p2p.R
import com.xpf.p2p.constants.Constants

/**
 * Created by Administrator on 2016/10/31.
 */
class GesturePoint(
    var leftX: Int,
    var rightX: Int,
    var topY: Int,
    var bottomY: Int,
    var image: ImageView,
    var num: Int
) {
    var centerX: Int = (leftX + rightX) / 2
    var centerY: Int = (topY + bottomY) / 2

    var pointState: Int = 0
        set(value) {
            field = value
            when (value) {
                Constants.POINT_STATE_NORMAL -> image.setBackgroundResource(R.drawable.gesture_node_normal)
                Constants.POINT_STATE_SELECTED -> image.setBackgroundResource(R.drawable.gesture_node_pressed)
                Constants.POINT_STATE_WRONG -> image.setBackgroundResource(R.drawable.gesture_node_wrong)
            }
        }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + bottomY
        result = prime * result + image.hashCode()
        result = prime * result + leftX
        result = prime * result + rightX
        result = prime * result + topY
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        other as GesturePoint
        if (bottomY != other.bottomY) return false
        if (image != other.image) return false
        if (leftX != other.leftX) return false
        if (rightX != other.rightX) return false
        if (topY != other.topY) return false
        return true
    }

    override fun toString(): String {
        return "Point [leftX=$leftX, rightX=$rightX, topY=$topY, bottomY=$bottomY]"
    }
}
