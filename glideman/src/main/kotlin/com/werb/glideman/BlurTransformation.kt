package com.werb.glideman

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.renderscript.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.lang.ref.SoftReference
import java.security.MessageDigest


/**
 * Created by wanbo on 2018/4/13.
 */
class BlurTransformation(context: Context, private val blurRadius: Float) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val weakContext = SoftReference(context)

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

        if (blurRadius !in 1..25){
            throw RSIllegalArgumentException("blurRadius out of range (0 < r <= 25).")
        }

        val bitmap = pool.get(outWidth, outWidth, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        weakContext.get()?.let {
            val renderScript = RenderScript.create(it)
            val input = Allocation.createFromBitmap(renderScript, alphaSafeBitmap)
            val output = Allocation.createTyped(renderScript, input.type)
            val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            scriptIntrinsicBlur.setRadius(blurRadius)
            scriptIntrinsicBlur.setInput(input)
            scriptIntrinsicBlur.forEach(output)
            output.copyTo(alphaSafeBitmap)
            output.destroy()
            input.destroy()
            scriptIntrinsicBlur.destroy()
            renderScript.destroy()
        }

        val matrix = getMatrix(outWidth, outHeight, alphaSafeBitmap.width, alphaSafeBitmap.height)
        canvas.drawBitmap(alphaSafeBitmap, matrix, getDefaultPaint())
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
        return other is BlurTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}