package com.xpf.p2p.adapter;

import android.view.View;
import android.widget.TextView;

import com.xpf.p2p.R;
import com.xpf.p2p.entity.Product;
import com.xpf.p2p.utils.UIUtils;
import com.xpf.p2p.widget.RoundProgress;

/**
 * Created by xpf on 2016/11/15 :)
 * Function:BaseHolder的实现类
 */

public class MyHolder extends BaseHolder<Product> {

    TextView pName;
    TextView pMoney;
    TextView pYearlv;
    TextView pSuodingdays;
    TextView pMinzouzi;
    TextView pMinnum;
    RoundProgress pProgresss;

    @Override
    protected void refreshData() {
        pName = (TextView) getRootView().findViewById(R.id.p_name);
        pMoney = (TextView) getRootView().findViewById(R.id.p_money);
        pYearlv = (TextView) getRootView().findViewById(R.id.p_yearlv);
        pSuodingdays = (TextView) getRootView().findViewById(R.id.p_suodingdays);
        pMinzouzi = (TextView) getRootView().findViewById(R.id.p_minzouzi);
        pMinnum = (TextView) getRootView().findViewById(R.id.p_minnum);
        pProgresss = (RoundProgress) getRootView().findViewById(R.id.p_progresss);
        Product data = getData();
        pName.setText(data.getName());
        pMinnum.setText(data.getMemberNum());
        pMoney.setText(data.getMoney());
        pYearlv.setText(data.getYearRate());
        pSuodingdays.setText(data.getSuodingDays());
        pMinzouzi.setText(data.getMinTouMoney());
        pProgresss.setProgress(Integer.parseInt(data.getProgress()));
    }

    @Override
    protected View initView() {
        return UIUtils.getView(R.layout.item_product_list);
    }
}
