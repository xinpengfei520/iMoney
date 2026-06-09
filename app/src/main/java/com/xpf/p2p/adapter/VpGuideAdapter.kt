package com.xpf.p2p.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by xpf on 2018/6/12 :)
 * Function:引导页图片适配器（ViewPager2 / RecyclerView）
 */
class VpGuideAdapter(private val resIds: IntArray) :
    RecyclerView.Adapter<VpGuideAdapter.GuideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val imageView = ImageView(parent.context).apply {
            // ViewPager2 requires each page to fill the pager.
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            )
        }
        return GuideViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.imageView.setBackgroundResource(resIds[position])
    }

    override fun getItemCount(): Int = resIds.size

    class GuideViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}
