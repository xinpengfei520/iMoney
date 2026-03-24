package com.xpf.p2p.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.databinding.ItemProductListBinding
import com.xpf.p2p.entity.Product
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/15 :)
 * Function:全部理财页面数据的适配器
 */
class ProductAdapter(private val list: List<Product>?) : BaseAdapter() {

    override fun getCount(): Int = (list?.size ?: 0) + 1

    override fun getItem(position: Int): Any? = list?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.e("TAG", parent.javaClass.toString())
        Log.e("TAG", parent.context.toString())

        var pos = position
        val itemViewType = getItemViewType(pos)
        if (itemViewType == 0) {
            val tv = TextView(parent.context)
            tv.text = "与子同游，动辄覆舟"
            tv.setTextColor(UIUtils.getColor(R.color.home_back_selected))
            tv.textSize = UIUtils.dp2px(20).toFloat()
            return tv
        }

        if (pos > 3) {
            pos--
        }

        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = ViewHolder(binding)
            view = binding.root
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val product = list!![pos]
        holder.binding.pName.text = product.name
        holder.binding.pMoney.text = product.money
        holder.binding.pYearlv.text = product.yearRate
        holder.binding.pMinnum.text = product.memberNum
        holder.binding.pMinzouzi.text = product.minTouMoney
        holder.binding.pSuodingdays.text = product.suodingDays
        holder.binding.pProgresss.progress = product.progress!!.toInt()

        return view
    }

    override fun getItemViewType(position: Int): Int = if (position == 3) 0 else 1

    override fun getViewTypeCount(): Int = 2

    private class ViewHolder(val binding: ItemProductListBinding)
}
