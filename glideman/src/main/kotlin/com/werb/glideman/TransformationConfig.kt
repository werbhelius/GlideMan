package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Created by wanbo on 2018/4/7.
 */
internal interface TransformationConfig {

    fun clear(canvas: Canvas) {
        canvas.setBitmap(null)
    }

    fun getAlphaSafeBitmap(
        pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
        val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
        if (safeConfig == maybeAlphaSafe.config) {
            return maybeAlphaSafe
        }
        val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, Bitmap.Config.RGB_565)
        Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f, 0f, null)
        return argbBitmap
    }

    fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Bitmap.Config.RGBA_F16 == inBitmap.config) {
                return Bitmap.Config.RGBA_F16
            }
        }
        return Bitmap.Config.ARGB_8888
    }


}