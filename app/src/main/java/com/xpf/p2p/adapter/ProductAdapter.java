package com.xpf.p2p.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xpf.p2p.R;
import com.xpf.p2p.entity.Product;
import com.xpf.p2p.utils.UIUtils;
import com.xpf.p2p.widget.RoundProgress;

import java.util.List;


/**
 * Created by xpf on 2016/11/15 :)
 * Function:全部理财页面数据的适配器
 */

public class ProductAdapter extends BaseAdapter {

    private List<Product> list;

    public ProductAdapter(List<Product> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("TAG", parent.getClass().toString());   // ListView
        Log.e("TAG", parent.getContext().toString()); // 加载ListView的Activity：MainActivity

        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            TextView tv = new TextView(parent.getContext());
            tv.setText("与子同游，动辄覆舟");
            tv.setTextColor(UIUtils.getColor(R.color.home_back_selected));
            tv.setTextSize(UIUtils.dp2px(20));
            return tv;
        }

        if (position > 3) {
            position--;
        }

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
        holder.pName.setText(product.getName());
        holder.pMoney.setText(product.getMoney());
        holder.pYearlv.setText(product.getYearRate());
        holder.pMinnum.setText(product.getMemberNum());
        holder.pMinzouzi.setText(product.getMinTouMoney());
        holder.pSuodingdays.setText(product.getSuodingDays());
        holder.pProgresss.setProgress(Integer.parseInt(product.getProgress()));

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 3) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
