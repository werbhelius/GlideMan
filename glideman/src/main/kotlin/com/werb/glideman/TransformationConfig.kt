package com.werb.glideman

import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Created by wanbo on 2018/4/7.
 */
internal interface TransformationConfig {

    fun clear(canvas: Canvas) {
        canvas.setBitmap(null)
    }

    fun getAlphaSafeBitmap(
        pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
        val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
        if (safeConfig == maybeAlphaSafe.config) {
            return maybeAlphaSafe
        }
        val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, Bitmap.Config.RGB_565)
        Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f, 0f, null)
        return argbBitmap
    }

    fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Bitmap.Config.RGBA_F16 == inBitmap.config) {
                return Bitmap.Config.RGBA_F16
            }
        }
        return Bitmap.Config.ARGB_8888
    }

    fun getPaint(targetWidth: Int, targetHeight: Int, alphaSafeBitmap: Bitmap) = Paint().apply {
        isAntiAlias = true
        isDither = true
        shader = getPaintShader(targetWidth, targetHeight, alphaSafeBitmap)
    }

    fun getBoardPaint(borderWidth: Float, borderColor: Int) = Paint().apply {
        isAntiAlias = true
        isDither = true
        strokeWidth = borderWidth
        color = borderColor
        style = Paint.Style.STROKE
    }

    private fun getPaintShader(targetWidth: Int, targetHeight: Int, alphaSafeBitmap: Bitmap): BitmapShader {
        val width = (alphaSafeBitmap.width - targetWidth) / 2f
        val height = (alphaSafeBitmap.height - targetHeight) / 2f
        val shader = BitmapShader(alphaSafeBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0f || height != 0f) {
            // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate(-width, -height)
            shader.setLocalMatrix(matrix)
        }
        return shader
    }

}