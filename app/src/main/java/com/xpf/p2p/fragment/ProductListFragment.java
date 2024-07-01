package com.xpf.p2p.fragment;

import android.text.TextUtils;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.xpf.common.base.BaseFragment;
import com.xpf.common.bean.Product;
import com.xpf.common.cons.ApiRequestUrl;
import com.xpf.common.utils.LogUtils;
import com.xpf.p2p.R;
import com.xpf.p2p.adapter.ProductAdapter;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Function:全部理财页面
 * 关于ListView使用的4个元素:①ListView ②集合数据 ③ Adapter ④ item_layout布局
 */

public class ProductListFragment extends BaseFragment {

    ListView product_listview;
    private List<Product> products;
    private static final String TAG = ProductListFragment.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected String getUrl() {
        return ApiRequestUrl.PRODUCT;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) { // content即为响应成功情况下,返回的product.json数据
        product_listview = mView.findViewById(R.id.product_listview);
        if (!TextUtils.isEmpty(content)) {
            LogUtils.d(TAG, content);
            // 解析json数据
            JSONObject jsonObject = JSON.parseObject(content);
            boolean isSuccess = jsonObject.getBoolean("success");
            if (isSuccess) {
                String data = jsonObject.getString("data");
                // 解析得到集合数据
                products = JSON.parseArray(data, Product.class);
                setAdapter();
            }
        } else {
            LogUtils.e(TAG, "content is null!");
        }
    }

    private void setAdapter() {
        if (products != null && !products.isEmpty()) {
            // 创建Adapter的实例并显示列表
            product_listview.setAdapter(new ProductAdapter(products));

            /*// 方式一:暴露getView()供子类实现,开发中可以使用
            ProductAdapter1 productAdapter1 = new ProductAdapter1(products);
            product_listview.setAdapter(productAdapter1);

            // 方式二:并没有使用ViewHolder减少findViewById()执行次数,开发中不可取
            ProductAdapter2 productAdapter2 = new ProductAdapter2(products);
            product_listview.setAdapter(productAdapter2);

            // 方式三:既使用了ViewHolder,同时实现了更简洁的抽取
            ProductAdapter3 productAdapter3 = new ProductAdapter3(products);
            product_listview.setAdapter(productAdapter3);*/
        }
    }

}
