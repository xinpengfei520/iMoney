package com.xpf.p2p.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.entity.Product
import com.xpf.p2p.widget.RoundProgress

/**
 * Created by xpf on 2016/11/15 :)
 * Function:继承父类并实现自己特有的getView()方法
 */
class ProductAdapter1(list: List<Product>?) : MyBaseAdapter1<Product>(list) {

    override fun myGetView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = View.inflate(parent.context, R.layout.item_product_list, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val product = list!![position]
        holder.pMinnum.text = product.memberNum
        holder.pMinzouzi.text = product.minTouMoney
        holder.pMoney.text = product.money
        holder.pName.text = product.name
        holder.pProgresss.progress = product.progress!!.toInt()
        holder.pSuodingdays.text = product.suodingDays
        holder.pYearlv.text = product.yearRate

        return view!!
    }

    private class ViewHolder(view: View) {
        val pName: TextView = view.findViewById(R.id.p_name)
        val pMoney: TextView = view.findViewById(R.id.p_money)
        val pYearlv: TextView = view.findViewById(R.id.p_yearlv)
        val pSuodingdays: TextView = view.findViewById(R.id.p_suodingdays)
        val pMinzouzi: TextView = view.findViewById(R.id.p_minzouzi)
        val pMinnum: TextView = view.findViewById(R.id.p_minnum)
        val pProgresss: RoundProgress = view.findViewById(R.id.p_progresss)
    }
}
