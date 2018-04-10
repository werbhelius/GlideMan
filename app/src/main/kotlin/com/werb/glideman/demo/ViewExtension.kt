package com.werb.glideman.demo

import android.content.res.Resources

/**
 * Created by wanbo on 2018/4/10.
 */

fun dip2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}