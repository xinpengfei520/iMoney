package com.xpf.p2p.adapter

import com.xpf.p2p.entity.Product

/**
 * Created by xpf on 2016/11/15 :)
 * Function:继承父类并实现自己特有的getView()和viewHolder方法,使代码更精简
 */
class ProductAdapter3(list: List<Product>?) : MyBaseAdapter3<Product>(list) {

    override fun getHolder(): BaseHolder<Product> = MyHolder()
}
