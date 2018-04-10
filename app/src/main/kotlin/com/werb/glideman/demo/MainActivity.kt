package com.werb.glideman.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.werb.glideman.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://1.bp.blogspot.com/-KpmNfEbcwGU/VM-lBioNnAI/AAAAAAAAAGc/0u-KQ_GfVBg/w1200-h630-p-k-no-nu/4512-TomopopWeek1OnePiece.gif"

        button.setOnClickListener {
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleCropTransformation())
                .into(imageView1)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleTransformation())
                .into(imageView2)
//            GlideApp.with(this)
//                .load(url)
//                .transition(withCrossFade())
//                .transform(CircleBorderTransformation(5f, resources.getColor(R.color.colorPrimaryDark)))
//                .into(imageView3)
//            GlideApp.with(this)
//                .load(url)
//                .transition(withCrossFade())
//                .transform(CircleBorderWithPaddingTransformation(
//                    5f,
//                    resources.getColor(R.color.colorPrimaryDark),
//                    5f,
//                    resources.getColor(android.R.color.white)))
//                .into(imageView4)
        }

        button2.setOnClickListener {
            GlideApp.with(this).clear(imageView1)
            GlideApp.with(this).clear(imageView2)
//            GlideApp.with(this).clear(imageView3)
//            GlideApp.with(this).clear(imageView4)
        }
    }
}
