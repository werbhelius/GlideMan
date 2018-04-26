package com.werb.glideman

import android.content.Context
import com.google.android.gms.vision.face.FaceDetector
import java.lang.ref.SoftReference

/**
 * Created by wanbo on 2018/4/22.
 */
object GlideManFaceUtils {

    private var weakContext: SoftReference<Context>? = null
    @Volatile
    private var faceDetector: FaceDetector? = null

    fun initialize(context: Context) {
        weakContext = SoftReference(context.applicationContext)
    }

    fun release() {
        faceDetector?.release()
        faceDetector = null
        weakContext = null
    }

    fun getFaceDetector() = initDetector()

    private fun initDetector(): FaceDetector {
        faceDetector?.let { return it } ?: run {
            synchronized(GlideManFaceUtils::class.java) {
                weakContext?.get()?.let {
                    val detector = FaceDetector.Builder(it)
                        .setTrackingEnabled(false)
                        .build()
                    faceDetector = detector
                    return detector
                } ?: throw RuntimeException("You have to call GlideManFaceUtils.initialize(context: Context) before use faceDetector.")
            }
        }
    }

}