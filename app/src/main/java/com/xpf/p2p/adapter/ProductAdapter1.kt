package com.xpf.p2p.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xpf.p2p.databinding.ItemProductListBinding
import com.xpf.p2p.entity.Product

/**
 * Created by xpf on 2016/11/15 :)
 * Function:继承父类并实现自己特有的getView()方法
 */
class ProductAdapter1(list: List<Product>?) : MyBaseAdapter1<Product>(list) {

    override fun myGetView(position: Int, convertView: View?, parent: ViewGroup): View {
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

        val product = list!![position]
        holder.binding.pMinnum.text = product.memberNum
        holder.binding.pMinzouzi.text = product.minTouMoney
        holder.binding.pMoney.text = product.money
        holder.binding.pName.text = product.name
        holder.binding.pProgresss.progress = product.progress!!.toInt()
        holder.binding.pSuodingdays.text = product.suodingDays
        holder.binding.pYearlv.text = product.yearRate

        return view
    }

    private class ViewHolder(val binding: ItemProductListBinding)
}
