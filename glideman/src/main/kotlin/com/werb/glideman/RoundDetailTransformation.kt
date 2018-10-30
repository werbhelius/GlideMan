package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import android.graphics.RectF

/**
 * Created by wanbo on 2018/4/8.
 */
class RoundDetailTransformation(leftTop: Float, rightTop: Float, leftBottom: Float, rightBottom: Float) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val radii = floatArrayOf(dip2px(leftTop).toFloat(), dip2px(leftTop).toFloat(), dip2px(rightTop).toFloat(), dip2px(rightTop).toFloat(), dip2px(leftBottom).toFloat(), dip2px(leftBottom).toFloat(), dip2px(rightBottom).toFloat(), dip2px(rightBottom).toFloat())

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

        val bitmap = pool.get(toTransform.width, toTransform.height, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        val paint = getShaderPaint(alphaSafeBitmap)
        val rect = RectF(0f, 0f, toTransform.width.toFloat(), toTransform.height.toFloat())
        val path = Path()
        path.addRoundRect(rect, radii, Path.Direction.CW)
        canvas.drawPath(path, paint)

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
        return other is RoundDetailTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}