package com.xpf.p2p.adapter

import android.util.Log
import android.view.View
import com.xpf.p2p.databinding.ItemProductListBinding
import com.xpf.p2p.entity.Product
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/15 :)
 * Function:继承父类并实现自己特有的getView()方法
 */
class ProductAdapter2(list: List<Product>?) : MyBaseAdapter2<Product>(list) {

    override fun setData(convertView: View, t: Product) {
        val binding = ItemProductListBinding.bind(convertView)
        binding.pName.text = t.name
        Log.e("TAG", "setData()")
    }

    override fun initView(): View = UIUtils.getView(com.xpf.p2p.R.layout.item_product_list)
}
