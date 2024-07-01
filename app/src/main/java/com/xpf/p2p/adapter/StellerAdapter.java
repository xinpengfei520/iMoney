package com.xpf.p2p.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.p2p.utils.UIUtils;
import com.xpf.p2p.widget.randomLayout.StellarMap;

import java.util.Random;

/**
 * Created by xpf on 2017/12/9 :)
 * Function:
 */

public class StellerAdapter implements StellarMap.Adapter {

    private Context context;
    private String[] datas = new String[]{"超级新手计划", "乐享活系列90天计划", "钱包计划", "30天理财计划(加息2%)", "90天理财计划(加息5%)", "180天理财计划(加息10%)",
            "林业局投资商业经营", "中学老师购买车辆", "屌丝下海经商计划", "新西游影视拍摄投资", "公司老板资金周转", "养猪场扩大经营",
            "旅游公司扩大规模", "阿里巴巴洗钱计划", "铁路局回款计划", "高级白领赢取白富美投资计划"};

    // 分为两组数据
    private String[] firstDatas = new String[datas.length / 2];
    private String[] secondDatas = new String[datas.length - datas.length / 2];
    private Random random;

    public StellerAdapter(Context context) {
        this.context = context;
        initData();
    }

    private void initData() {
        // 给两组数组数据赋值
        for (int i = 0; i < datas.length; i++) {
            if (i < datas.length / 2) {
                firstDatas[i] = datas[i];
            } else {
                secondDatas[i - datas.length / 2] = datas[i];
            }
        }
    }

    // 返回显示的组数
    @Override
    public int getGroupCount() {
        return 2;
    }

    // 返回指定组的元素的个数
    @Override
    public int getCount(int group) {
        if (group == 0) {
            return datas.length / 2;
        } else {
            return datas.length - datas.length / 2;
        }
    }

    // 返回指定组的指定位置上的view,对于每组数据来讲position都从0开始
    @Override
    public View getView(int group, int position, View convertView) {
        random = new Random();
        final TextView tv = new TextView(context);
        if (group == 0) {
            tv.setText(firstDatas[position]);
        } else {
            tv.setText(secondDatas[position]);
        }

        // 提供随机的三色
        int red = random.nextInt(200);
        int green = random.nextInt(200);
        int blue = random.nextInt(200);

        tv.setTextColor(Color.rgb(red, green, blue));
        tv.setTextSize(random.nextInt(UIUtils.dp2px(10)) + UIUtils.dp2px(5));

        //给TextView设置监听
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "context = " + context);//MainActivity
                Toast.makeText(context, tv.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        return tv;
    }

    // 下一组显示平移动画的组别,查看源代码发现,此方法从未被调用,不用重写
    @Override
    public int getNextGroupOnPan(int group, float degree) {
        return 0;
    }

    // 下一组显示缩放动画的组别
    @Override
    public int getNextGroupOnZoom(int group, boolean isZoomIn) {
        if (group == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
