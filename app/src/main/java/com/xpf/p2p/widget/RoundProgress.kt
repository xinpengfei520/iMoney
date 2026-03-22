package com.xpf.p2p.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xpf.p2p.R
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:vancexin
 * Function:自定义圆环进度的动态更新显示
 */
class RoundProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint
    private var width: Int = 0

    var max: Int
    var progress: Int
    private val roundColor: Int
    private val roundProgressColor: Int
    private val textColor: Int
    private val textSize: Int
    private val roundWidth: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress)
        roundColor = typedArray.getColor(R.styleable.RoundProgress_roundColor, Color.GRAY)
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgress_roundProgressColor, Color.RED)
        textColor = typedArray.getColor(R.styleable.RoundProgress_textColor, Color.GREEN)
        roundWidth = typedArray.getDimension(R.styleable.RoundProgress_roundWidth, UIUtils.dp2px(10).toFloat()).toInt()
        textSize = typedArray.getDimension(R.styleable.RoundProgress_textSize, UIUtils.dp2px(20).toFloat()).toInt()
        progress = typedArray.getInteger(R.styleable.RoundProgress_progress, 40)
        max = typedArray.getInteger(R.styleable.RoundProgress_max, 100)
        typedArray.recycle()

        paint = Paint()
        paint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = this.measuredWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dx = width / 2
        val dy = width / 2
        val radius = width / 2 - roundWidth / 2
        paint.color = roundColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = roundWidth.toFloat()
        canvas.drawCircle(dx.toFloat(), dy.toFloat(), radius.toFloat(), paint)

        val rectF = RectF(
            (roundWidth / 2).toFloat(), (roundWidth / 2).toFloat(),
            (width - roundWidth / 2).toFloat(), (width - roundWidth / 2).toFloat()
        )
        paint.color = roundProgressColor
        canvas.drawArc(rectF, 0f, (progress * 360 / max).toFloat(), false, paint)

        paint.color = textColor
        paint.textSize = textSize.toFloat()
        paint.strokeWidth = 0f
        val text = "${progress * 100 / max}%"
        val bound = Rect()
        paint.getTextBounds(text, 0, text.length, bound)

        val left = width / 2 - bound.width() / 2
        val bottom = width / 2 + bound.height() / 2

        canvas.drawText(text, left.toFloat(), bottom.toFloat(), paint)
    }
}
