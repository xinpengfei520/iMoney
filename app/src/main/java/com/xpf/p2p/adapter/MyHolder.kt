package com.xpf.p2p.adapter

import android.view.View
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.entity.Product
import com.xpf.p2p.utils.UIUtils
import com.xpf.p2p.widget.RoundProgress

/**
 * Created by xpf on 2016/11/15 :)
 * Function:BaseHolder的实现类
 */
class MyHolder : BaseHolder<Product>() {

    private lateinit var pName: TextView
    private lateinit var pMoney: TextView
    private lateinit var pYearlv: TextView
    private lateinit var pSuodingdays: TextView
    private lateinit var pMinzouzi: TextView
    private lateinit var pMinnum: TextView
    private lateinit var pProgresss: RoundProgress

    override fun refreshData() {
        pName = rootView.findViewById(R.id.p_name)
        pMoney = rootView.findViewById(R.id.p_money)
        pYearlv = rootView.findViewById(R.id.p_yearlv)
        pSuodingdays = rootView.findViewById(R.id.p_suodingdays)
        pMinzouzi = rootView.findViewById(R.id.p_minzouzi)
        pMinnum = rootView.findViewById(R.id.p_minnum)
        pProgresss = rootView.findViewById(R.id.p_progresss)
        val product = data!!
        pName.text = product.name
        pMinnum.text = product.memberNum
        pMoney.text = product.money
        pYearlv.text = product.yearRate
        pSuodingdays.text = product.suodingDays
        pMinzouzi.text = product.minTouMoney
        pProgresss.progress = product.progress!!.toInt()
    }

    override fun initView(): View = UIUtils.getView(R.layout.item_product_list)
}
