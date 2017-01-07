package com.xpf.p2p.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.p2p.R;
import com.xpf.p2p.common.BaseFragment;
import com.xpf.p2p.ui.FlowLayout;
import com.xpf.p2p.utils.DrawUtils;
import com.xpf.p2p.utils.UIUtils;
import com.loopj.android.http.RequestParams;

import java.util.Random;

import butterknife.BindView;

/**
 * Created by xpf on 2016/11/15 :)
 * Wechat:18091383534
 * Function:热门理财页面
 */

public class ProductHotFragment extends BaseFragment {

    @BindView(R.id.flow_layout)
    FlowLayout flowLayout;

    private String[] datas = new String[]{"新手计划", "乐享活系列90天计划", "钱包", "30天理财计划(加息2%)",
            "林业局投资商业经营与大捞一笔", "中学老师购买车辆", "屌丝下海经商计划", "新西游影视拍", "Java培训老师自己周转",
            "HelloWorld", "C++-C-ObjectC-java", "Android vs ios", "算法与数据结构", "JNI与NDK", "team working"};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_hot;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {

        Random random = new Random();
        for (int i = 0; i < datas.length; i++) {
            final TextView tv = new TextView(getActivity());
            tv.setText(datas[i]);
            tv.setTextSize(UIUtils.dp2px(10));
            // 提供边距的对象,并设置到textView中
            ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mp.leftMargin = UIUtils.dp2px(5);
            mp.topMargin = UIUtils.dp2px(5);
            mp.rightMargin = UIUtils.dp2px(5);
            mp.bottomMargin = UIUtils.dp2px(5);
            tv.setLayoutParams(mp);
            // 设置背景
            int red = random.nextInt(210);
            int green = random.nextInt(210);
            int blue = random.nextInt(210);

            // 方式一：
            tv.setBackground(DrawUtils.getDrawable(Color.rgb(red, green, blue), UIUtils.dp2px(5)));

            // 方式二：
            tv.setBackground(DrawUtils.getSelector(DrawUtils.getDrawable(Color.rgb(red, green, blue), UIUtils.dp2px(5)), DrawUtils.getDrawable(Color.WHITE, UIUtils.dp2px(5))));
            // 当设置了点击事件时,默认textView就是可点击的了
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProductHotFragment.this.getActivity(), tv.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            // 设置内边距：
            int padding = UIUtils.dp2px(5);
            tv.setPadding(padding, padding, padding, padding);

            flowLayout.addView(tv);
        }
    }

}
