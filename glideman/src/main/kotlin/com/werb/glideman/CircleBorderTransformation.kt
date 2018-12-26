package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/4/9.
 */
class CircleBorderTransformation(borderWidth: Float, private val borderColor: Int) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val borderWidthPx = dip2px(borderWidth)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // find mix edge
        val destMinEdge = Math.min(outWidth, outHeight)
        // circle radius
        val radius = destMinEdge / 2f

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius - borderWidthPx, getShaderPaint(destMinEdge - 2 * borderWidthPx, destMinEdge - 2 * borderWidthPx, alphaSafeBitmap))
        // draw border
        val boardPaint = getBoardPaint(borderWidthPx.toFloat(), borderColor)
        canvas.drawCircle(radius, radius, radius - borderWidthPx / 2f, boardPaint)

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
        return other is CircleBorderTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}