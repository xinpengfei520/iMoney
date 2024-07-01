package com.xpf.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpf.p2p.R;
import com.xpf.p2p.entity.Product;
import com.xpf.p2p.widget.RoundProgress;

import java.util.List;

/**
 * Created by xpf on 2016/11/15 :)
 * Function:继承父类并实现自己特有的getView()方法
 */

public class ProductAdapter1 extends MyBaseAdapter1<Product> {

    public ProductAdapter1(List<Product> list) {
        super(list);
    }

    // 装配数据
    @Override
    protected View MyGetView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = list.get(position);

        // 装配数据
        holder.pMinnum.setText(product.getMemberNum());
        holder.pMinzouzi.setText(product.getMinTouMoney());
        holder.pMoney.setText(product.getMoney());
        holder.pName.setText(product.getName());
        holder.pProgresss.setProgress(Integer.parseInt(product.getProgress()));
        holder.pSuodingdays.setText(product.getSuodingDays());
        holder.pYearlv.setText(product.getYearRate());

        return convertView;
    }

    static class ViewHolder {
        TextView pName;
        TextView pMoney;
        TextView pYearlv;
        TextView pSuodingdays;
        TextView pMinzouzi;
        TextView pMinnum;
        RoundProgress pProgresss;

        ViewHolder(View view) {
            pName = (TextView) view.findViewById(R.id.p_name);
            pMoney = (TextView) view.findViewById(R.id.p_money);
            pYearlv = (TextView) view.findViewById(R.id.p_yearlv);
            pSuodingdays = (TextView) view.findViewById(R.id.p_suodingdays);
            pMinzouzi = (TextView) view.findViewById(R.id.p_minzouzi);
            pMinnum = (TextView) view.findViewById(R.id.p_minnum);
            pProgresss = (RoundProgress) view.findViewById(R.id.p_progresss);
        }
    }
}
