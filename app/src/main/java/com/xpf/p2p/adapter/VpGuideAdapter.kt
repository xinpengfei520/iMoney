package com.xpf.p2p.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by xpf on 2018/6/12 :)
 * Function:
 */
class VpGuideAdapter(private val mImageViews: ArrayList<ImageView>) : PagerAdapter() {

    override fun getCount(): Int = mImageViews.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = mImageViews[position]
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
