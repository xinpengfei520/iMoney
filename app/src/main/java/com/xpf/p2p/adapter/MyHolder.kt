package com.xpf.p2p.adapter

import android.view.View
import com.xpf.p2p.databinding.ItemProductListBinding
import com.xpf.p2p.entity.Product
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/15 :)
 * Function:BaseHolder的实现类
 */
class MyHolder : BaseHolder<Product>() {

    private lateinit var binding: ItemProductListBinding

    override fun refreshData() {
        binding = ItemProductListBinding.bind(rootView)
        val product = data!!
        binding.pName.text = product.name
        binding.pMinnum.text = product.memberNum
        binding.pMoney.text = product.money
        binding.pYearlv.text = product.yearRate
        binding.pSuodingdays.text = product.suodingDays
        binding.pMinzouzi.text = product.minTouMoney
        binding.pProgresss.progress = product.progress!!.toInt()
    }

    override fun initView(): View = UIUtils.getView(com.xpf.p2p.R.layout.item_product_list)
}
