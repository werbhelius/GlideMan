package com.werb.glideman

import android.graphics.Bitmap
import android.graphics.PointF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import java.security.MessageDigest


/**
 * Created by wanbo on 2018/4/22.
 */
class FaceTransformation: BitmapTransformation(), TransformationConfig {

    private val id = this::class.java.name

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = pool.get(outWidth, outWidth, getAlphaSafeConfig(toTransform)).apply { setHasAlpha(true) }
        val alphaSafeBitmap = getAlphaSafeBitmap(pool, toTransform)

    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(id.toByteArray())
    }

    private fun detectFace(bitmap: Bitmap, centerOfAllFaces: PointF) {
        val faceDetector = GlideManFaceUtils.getFaceDetector()
        if (!faceDetector.isOperational) {
            centerOfAllFaces.set((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat()) // center crop
            return
        }
        val frame = Frame.Builder().setBitmap(bitmap).build()
        val faces = faceDetector.detect(frame)
        val totalFaces = faces.size()
        if (totalFaces > 0) {
            var sumX = 0f
            var sumY = 0f
            for (i in 0 until totalFaces) {
                val faceCenter = PointF()
                getFaceCenter(faces.get(faces.keyAt(i)), faceCenter)
                sumX += faceCenter.x
                sumY += faceCenter.y
            }
            centerOfAllFaces.set(sumX / totalFaces, sumY / totalFaces)
            return
        }
        centerOfAllFaces.set((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat()) // center crop
    }

    private fun getFaceCenter(face: Face, center: PointF) {
        val x = face.position.x
        val y = face.position.y
        val width = face.width
        val height = face.height
        center.set(x + width / 2, y + height / 2)
    }

    override fun equals(other: Any?): Boolean {
        return other is FaceTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}