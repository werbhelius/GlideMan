package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/4/9.
 */
class CircleBorderWithPaddingTransformation(borderWidth: Float,
                                            private val borderColor: Int,
                                            paddingWidth: Float,
                                            private val paddingColor: Int) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val borderWidthPx = dip2px(borderWidth)
    private val paddingWidthPx = dip2px(paddingWidth)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // find mix edge
        val destMinEdge = Math.min(toTransform.width, toTransform.height)
        // circle radius
        val radius = destMinEdge / 2f
        // diff
        val diff = borderWidthPx + paddingWidthPx

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius - diff + 1, getShaderPaint(destMinEdge - 2 * diff, destMinEdge - 2 * diff, alphaSafeBitmap))
        // draw border
        val boardPaint = getBoardPaint(borderWidthPx.toFloat(), borderColor)
        canvas.drawCircle(radius, radius, radius - borderWidthPx.toFloat() / 2f, boardPaint)
        // draw padding
        val paddingPaint = getBoardPaint(paddingWidthPx.toFloat(), paddingColor)
        canvas.drawCircle(radius, radius, radius - borderWidthPx - paddingWidthPx.toFloat() / 2f, paddingPaint)
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
        return other is CircleBorderWithPaddingTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}