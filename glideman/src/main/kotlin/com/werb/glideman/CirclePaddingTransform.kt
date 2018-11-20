package com.werb.glideman

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.werb.glideman.CircleTransformation
import com.werb.glideman.TransformationConfig
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/11/20.
 */
class CirclePaddingTransform(padding: Float) : BitmapTransformation(), TransformationConfig {

    private val paddingDp = dip2px(padding)
    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // find mix edge
        val destMinEdge = Math.min(outWidth, outWidth)
        // circle radius
        val radius = (destMinEdge - 2 * paddingDp).toFloat() / 2f

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)

        // 计算缩放比例
        val scale = (destMinEdge - 2 * paddingDp).toFloat() / destMinEdge

        val canvas = Canvas(bitmap)
        canvas.drawCircle(destMinEdge.toFloat() / 2, destMinEdge.toFloat() / 2, destMinEdge.toFloat() / 2, Paint().apply {
            isAntiAlias = true
            isDither = true
            shader = BitmapShader(alphaSafeBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
                setLocalMatrix(Matrix().apply {
                    setScale(scale, scale)
                    postTranslate(paddingDp.toFloat(), paddingDp.toFloat())
                })
            }
        })
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