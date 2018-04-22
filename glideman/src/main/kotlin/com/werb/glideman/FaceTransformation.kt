package com.werb.glideman

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by wanbo on 2018/4/22.
 */
class FaceTransformation: BitmapTransformation() {

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray())
    }

    override fun equals(other: Any?): Boolean {
        return other is FaceTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}