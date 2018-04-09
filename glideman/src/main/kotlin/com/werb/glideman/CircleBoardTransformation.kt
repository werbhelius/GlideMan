package com.werb.glideman

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/4/9.
 */
class CircleBoardTransformation(private val boardWidth: Int, private val boardColor: Int) : BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name
    private val boardWidthFloat = Resources.getSystem().displayMetrics.density * boardWidth

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // find mix edge
        val destMinEdge = Math.min(toTransform.width, toTransform.height)
        // circle radius
        val radius = destMinEdge / 2f

        val bitmap = pool.get(destMinEdge, destMinEdge, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius - 2 * boardWidth, getPaint(destMinEdge - 2 * boardWidth, destMinEdge - 2 * boardWidth, alphaSafeBitmap))
        // draw board
        val boardPaint = getBoardPaint(boardWidthFloat, boardColor)
        canvas.drawCircle(radius, radius, radius - 2 * boardWidth, boardPaint)

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
        return other is CircleBoardTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}