package com.xpf.p2p.adapter;

import com.xpf.p2p.bean.Product;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Wechat:18091383534
 * Function:继承父类并实现自己特有的getView()和viewHolder方法,使代码更精简
 */

public class ProductAdapter3 extends MyBaseAdapter3<Product> {

    public ProductAdapter3(List<Product> list) {
        super(list);
    }

    @Override
    protected BaseHolder getHolder() {
        return new MyHolder();
    }
}
