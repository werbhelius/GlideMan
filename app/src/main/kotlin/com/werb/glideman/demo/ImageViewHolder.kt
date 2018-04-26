package com.werb.glideman.demo

import android.graphics.Color
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.werb.glideman.*
import com.werb.library.MoreViewHolder
import com.werb.library.link.LayoutID
import kotlinx.android.synthetic.main.layout_image_item.*

/**
 * Created by wanbo on 2018/4/22.
 */
@LayoutID(R.layout.layout_image_item)
class ImageViewHolder(containerView: View) : MoreViewHolder<ImageItem>(containerView) {

    override fun bindData(data: ImageItem, payloads: List<Any>) {
        val request = GlideApp.with(containerView)
            .load(data.url)
        when(layoutPosition){
            0 -> {
                request.transform(CircleTransformation())
            }
            1 -> {
                request.transform(RoundTransformation(5f))
            }
            2 -> {
                request.transform(CircleBorderTransformation(5f, Color.parseColor("#FD7013")))
            }
            3 -> {
                request.transform(CircleBorderWithPaddingTransformation(
                    5f,
                    Color.parseColor("#FD7013"),
                    5f,
                    Color.parseColor("#FFFFFF")))
            }
            4 -> {
                request.transform(MaskColorTransformation(Color.parseColor("#39FF5F5F")))
            }
            5 -> {
                request.transform(MaskShapeTransformation(containerView.context.resources.getDrawable(R.drawable.im_to_message_bg)))
            }
            6 -> {
                request.transforms(CenterCrop(),PixelTransformation(3f))
            }
            7 -> {
                request.transform(BlurTransformation(containerView.context, 4f))
            }
            8 -> {
                request.transform(RoundBorderTransformation(5f, 5f, Color.parseColor("#FD7013")))
            }
        }
        request.into(image)
    }
}