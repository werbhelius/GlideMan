package com.werb.glideman

import android.graphics.*
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.lang.ref.SoftReference
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/4/12.
 */
class MaskShapeTransformation(private val drawable: Drawable, private val mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN ): BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val weakDrawable = SoftReference(drawable)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = pool.get(outWidth, outWidth, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        weakDrawable.get()?.setBounds(0, 0, outWidth, outHeight)
        weakDrawable.get()?.draw(canvas)
        val matrix = getMatrix(outWidth, outHeight, alphaSafeBitmap.width, alphaSafeBitmap.height)
        canvas.drawBitmap(alphaSafeBitmap, matrix, getDefaultPaint().apply { xfermode = PorterDuffXfermode(mode) })
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
        return other is MaskShapeTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}