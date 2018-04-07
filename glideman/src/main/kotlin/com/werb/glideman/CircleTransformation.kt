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
        val destMinEdge = Math.min(toTransform.width, toTransform.height)
        // circle radius
        val radius = destMinEdge / 2f
        val width = (toTransform.width - destMinEdge) / 2f
        val height = (toTransform.height - destMinEdge) / 2f

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val paint = Paint().apply { isAntiAlias = true }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val shader = BitmapShader(alphaSafeBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0f || height != 0f) {
            // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate(-width, -height)
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius, paint)
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
        return other is CircleTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}