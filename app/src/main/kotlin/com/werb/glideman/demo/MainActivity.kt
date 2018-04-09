package com.werb.glideman.demo

import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.werb.glideman.CircleTransformation
import com.werb.glideman.RoundTransformation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1522747631824&di=64639a5ee5a9f7656c62564a95d02fe8&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201504%2F26%2F201504261434_tRzA4.png"

        button.setOnClickListener {
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(RoundTransformation(5))
                .into(imageView)
        }

        button2.setOnClickListener {
            GlideApp.with(this).clear(imageView)
        }
    }
}
