package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import android.graphics.RectF


/**
 * Created by wanbo on 2018/4/13.
 */
class PixelTransformation(blockSize: Float = 1f) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val blockSizeFloat = dip2px(blockSize)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = pool.get(outWidth, outWidth, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)

        (0 until outWidth).forEach { width ->
            (0 until outHeight).forEach { height ->
                val pixelCoordX = blockSizeFloat * width
                val pixelCoordY = blockSizeFloat * height
                if (pixelCoordX + blockSizeFloat < outWidth && pixelCoordY + blockSizeFloat < outHeight) {
                    val midX = pixelCoordX + blockSizeFloat / 2
                    val midY = pixelCoordY + blockSizeFloat / 2
                    val paint = getDefaultPaint(alphaSafeBitmap.getPixel(midX, midY))
                    val rectF = RectF(pixelCoordX.toFloat(), pixelCoordY.toFloat(), pixelCoordX.toFloat() + blockSizeFloat, pixelCoordY.toFloat() + blockSizeFloat)
                    canvas.drawRect(rectF, paint)
                }
            }
        }

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
        return other is PixelTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}