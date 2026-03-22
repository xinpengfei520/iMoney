package com.xpf.p2p.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

/**
 * Created by xpf on 2016/11/18 :)
 * Wechat:vancexin
 * Function:Bitmap对象的处理类
 */
object BitmapUtils {

    @JvmStatic
    fun circleBitmap(source: Bitmap): Bitmap {
        val width = source.width
        val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawCircle((width / 2).toFloat(), (width / 2).toFloat(), (width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, 0f, 0f, paint)
        return bitmap
    }

    @JvmStatic
    fun zoom(source: Bitmap, width: Float, height: Float): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val matrix = Matrix()
            val scaleX = width / source.width
            val scaleY = height / source.height
            matrix.postScale(scaleX, scaleY)
            bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}
