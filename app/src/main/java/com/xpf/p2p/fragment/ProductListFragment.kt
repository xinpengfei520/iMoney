package com.xpf.p2p.fragment

import android.text.TextUtils
import android.widget.ListView
import com.alibaba.fastjson.JSON
import com.xpf.p2p.R
import com.xpf.p2p.adapter.ProductAdapter
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.constants.ApiRequestUrl
import com.xpf.p2p.entity.Product
import com.xpf.p2p.utils.LogUtils

/**
 * Created by xpf on 2016/11/15 :)
 * Function:全部理财页面
 */
class ProductListFragment : BaseFragment() {

    private lateinit var productListview: ListView
    private var products: List<Product>? = null

    override fun getLayoutId(): Int = R.layout.fragment_product_list

    override fun getUrl(): String = ApiRequestUrl.PRODUCT

    override fun getParams(): Map<String, String>? = null

    override fun initData(content: String?) {
        productListview = mView!!.findViewById(R.id.product_listview)
        if (!TextUtils.isEmpty(content)) {
            LogUtils.d(TAG, content)
            val jsonObject = JSON.parseObject(content)
            val isSuccess = jsonObject.getBoolean("success")
            if (isSuccess) {
                val data = jsonObject.getString("data")
                products = JSON.parseArray(data, Product::class.java)
                setAdapter()
            }
        } else {
            LogUtils.e(TAG, "content is null!")
        }
    }

    private fun setAdapter() {
        if (!products.isNullOrEmpty()) {
            productListview.adapter = ProductAdapter(products)
        }
    }

    companion object {
        private val TAG = ProductListFragment::class.java.simpleName
    }
}
