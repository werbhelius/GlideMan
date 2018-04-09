package com.werb.glideman

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import android.graphics.RectF

/**
 * Created by wanbo on 2018/4/8.
 */
class RoundTransformation(corner: Int) : BitmapTransformation(), TransformationConfig {

    private val cornerFloat = Resources.getSystem().displayMetrics.density * corner

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

        val targetWidth = toTransform.width
        val targetHeight = toTransform.height

        val bitmap = pool.get(targetWidth, targetHeight, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        val paint = getPaint(targetWidth, targetWidth, alphaSafeBitmap)
        val rectF = RectF(0f, 0f, targetWidth.toFloat(), targetWidth.toFloat())
        canvas.drawRoundRect(rectF, cornerFloat, cornerFloat, paint)
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
        return other is RoundTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}