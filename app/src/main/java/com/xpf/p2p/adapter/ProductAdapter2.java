package com.xpf.p2p.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xpf.p2p.R;
import com.xpf.p2p.entity.Product;
import com.xpf.p2p.utils.UIUtils;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Function:继承父类并实现自己特有的getView()方法
 */

public class ProductAdapter2 extends MyBaseAdapter2<Product> {

    public ProductAdapter2(List<Product> list) {
        super(list);
    }

    @Override
    protected void setData(View convertView, Product product) {
        ((TextView) convertView.findViewById(R.id.p_name)).setText(product.getName());

        // 一共需要提供7项的赋值此方法会产生过多的findviewById操作
        Log.e("TAG", "setData()");
    }

    @Override
    protected View initView() {
        return UIUtils.getView(R.layout.item_product_list);
    }
}
