package com.xpf.p2p.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.entity.Product
import com.xpf.p2p.utils.UIUtils
import com.xpf.p2p.widget.RoundProgress

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
            view = View.inflate(parent.context, R.layout.item_product_list, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val product = list!![pos]
        holder.pName.text = product.name
        holder.pMoney.text = product.money
        holder.pYearlv.text = product.yearRate
        holder.pMinnum.text = product.memberNum
        holder.pMinzouzi.text = product.minTouMoney
        holder.pSuodingdays.text = product.suodingDays
        holder.pProgresss.progress = product.progress!!.toInt()

        return view!!
    }

    override fun getItemViewType(position: Int): Int = if (position == 3) 0 else 1

    override fun getViewTypeCount(): Int = 2

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
