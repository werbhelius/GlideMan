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

        val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523375013640&di=b8f06820750af771d000e8d98f7133a4&imgtype=0&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2Fee159390c4cafb3b0fe5894a955d56550bc483d9.jpg"

        button.setOnClickListener {
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transforms(MaskShapeTransformation(resources.getDrawable(R.drawable.im_to_message_bg)))
                .into(imageView1)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(RoundBorderTransformation(10f, 5f, resources.getColor(R.color.colorPrimaryDark)))
                .into(imageView2)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleBorderTransformation(5f, resources.getColor(R.color.colorPrimaryDark)))
                .into(imageView3)
            GlideApp.with(this)
                .load(url)
                .transition(withCrossFade())
                .transform(CircleBorderWithPaddingTransformation(
                    5f,
                    resources.getColor(R.color.colorPrimaryDark),
                    5f,
                    resources.getColor(android.R.color.white)))
                .into(imageView4)
        }

        button2.setOnClickListener {
            GlideApp.with(this).clear(imageView1)
            GlideApp.with(this).clear(imageView2)
            GlideApp.with(this).clear(imageView3)
            GlideApp.with(this).clear(imageView4)
        }
    }
}
