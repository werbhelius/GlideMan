package com.werb.glideman.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.werb.glideman.*
import com.werb.library.MoreAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter: MoreAdapter by lazy {
        MoreAdapter().apply {
            register(ImageViewHolder::class.java)
            attachTo(recyclerView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        (1..9).forEach {
            adapter.loadData(ImageItem())
        }

    }
}
