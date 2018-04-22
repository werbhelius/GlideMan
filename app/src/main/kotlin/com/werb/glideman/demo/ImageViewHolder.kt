package com.werb.glideman.demo

import android.view.View
import com.werb.glideman.RoundTransformation
import com.werb.library.MoreViewHolder
import com.werb.library.link.LayoutID
import kotlinx.android.synthetic.main.layout_image_item.*

/**
 * Created by wanbo on 2018/4/22.
 */
@LayoutID(R.layout.layout_image_item)
class ImageViewHolder(containerView: View) : MoreViewHolder<ImageItem>(containerView) {

    override fun bindData(data: ImageItem, payloads: List<Any>) {
        GlideApp.with(containerView)
            .load(data.url)
            .transform(RoundTransformation(5f))
            .into(image)
    }
}