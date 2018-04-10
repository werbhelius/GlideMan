package com.werb.glideman

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * Created by wanbo on 2018/4/7.
 */
class CircleCropTransformation : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // find mix edge
        val destMinEdge = Math.min(outWidth, outWidth)
        // circle radius
        val radius = destMinEdge / 2f

        val srcWidth = toTransform.width
        val srcHeight = toTransform.height

        val scaleX = destMinEdge / srcWidth.toFloat()
        val scaleY = destMinEdge / srcHeight.toFloat()
        val maxScale = Math.max(scaleX, scaleY)

        val scaledWidth = maxScale * srcWidth
        val scaledHeight = maxScale * srcHeight
        val left = (destMinEdge - scaledWidth) / 2f
        val top = (destMinEdge - scaledHeight) / 2f

        val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius, Paint().apply {
            isAntiAlias = true
            isDither = true
            isFilterBitmap = true
        })
        canvas.drawBitmap(alphaSafeBitmap, null, destRect, Paint().apply {
            isAntiAlias = true
            isDither = true
            isFilterBitmap = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        })
        clear(canvas)

        // save in pool to reuse
        if (alphaSafeBitmap != toTransform) {
            pool.put(toTransform)
        }
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray())
    }

    override fun equals(other: Any?): Boolean {
        return other is CircleCropTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}