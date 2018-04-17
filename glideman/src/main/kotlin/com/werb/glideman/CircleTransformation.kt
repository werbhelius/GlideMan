package com.werb.glideman

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * Created by wanbo on 2018/4/7.
 */
class CircleTransformation : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // find mix edge
        val destMinEdge = Math.min(outWidth, outWidth)
        // circle radius
        val radius = destMinEdge / 2f

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius, getShaderPaint(destMinEdge, destMinEdge, alphaSafeBitmap))
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
        return other is CircleTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}