package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import android.graphics.RectF

/**
 * Created by wanbo on 2018/4/8.
 */
class RoundBorderTransformation(corner: Float, borderWidth: Float, private val borderColor: Int) : BitmapTransformation(), TransformationConfig {

    private val cornerFloat = dip2px(corner)
    private val borderWidthPx = dip2px(borderWidth)

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

        val bitmap = pool.get(outWidth, outHeight, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        val paint = getShaderPaint(outWidth, outHeight, alphaSafeBitmap)
        val rectF = RectF(0f, 0f, outWidth.toFloat(), outHeight.toFloat())
        canvas.drawRoundRect(rectF, cornerFloat.toFloat(), cornerFloat.toFloat(), paint)
        val boardPaint = getBoardPaint(borderWidthPx.toFloat(), borderColor)
        val rectF2 = RectF(borderWidthPx / 2f, borderWidthPx / 2f, outWidth.toFloat() - borderWidthPx / 2f, outHeight.toFloat() - borderWidthPx / 2f)
        canvas.drawRoundRect(rectF2, cornerFloat - borderWidthPx / 2f, cornerFloat - borderWidthPx / 2f, boardPaint)
        clear(canvas)

        // save in pool to reuse
        if (alphaSafeBitmap != toTransform) {
            pool.put(alphaSafeBitmap)
        }
        return bitmap

    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray())
    }

    override fun equals(other: Any?): Boolean {
        return other is RoundBorderTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}