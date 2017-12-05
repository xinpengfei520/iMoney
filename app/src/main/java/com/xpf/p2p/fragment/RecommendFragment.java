package com.xpf.p2p.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.p2p.R;
import com.xpf.p2p.common.BaseFragment;
import com.xpf.p2p.ui.randomLayout.StellarMap;
import com.xpf.p2p.utils.UIUtils;
import com.loopj.android.http.RequestParams;

import java.util.Random;

import butterknife.BindView;

/**
 * Created by xpf on 2016/11/15 :)
 * Wechat:18091383534
 * Function:推荐理财页面
 * 总结:关于布局中视图的添加的方式:
 * 1.如果在布局文件中：直接在其内部声明子标签即可
 * 2.如果是在java代码中：方式一：通过布局调用addView() 方式二：通过setAdapter()
 */

public class RecommendFragment extends BaseFragment {

    @BindView(R.id.stellarMap)
    StellarMap stellarMap;

    private String[] datas = new String[]{"超级新手计划", "乐享活系列90天计划", "钱包计划", "30天理财计划(加息2%)", "90天理财计划(加息5%)", "180天理财计划(加息10%)",
            "林业局投资商业经营", "中学老师购买车辆", "屌丝下海经商计划", "新西游影视拍摄投资", "Java培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "阿里巴巴洗钱计划", "铁路局回款计划", "高级白领赢取白富美投资计划"
    };

    // 分为两组数据
    private String[] firstDatas = new String[datas.length / 2];
    private String[] secondDatas = new String[datas.length - datas.length / 2];
    private Random random;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_recommond;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    @Override
    protected void initData(String content) {

        // 给两组数组数据赋值
        for (int i = 0; i < datas.length; i++) {
            if (i < datas.length / 2) {
                firstDatas[i] = datas[i];
            } else {
                secondDatas[i - datas.length / 2] = datas[i];
            }
        }

        StellerAdapter stellerAdapter = new StellerAdapter();
        // 加载显示
        stellarMap.setAdapter(stellerAdapter);

        // 必须提供如下两个方法的调用，否则没有显示效果
        // 设置初始化显示的组别,以及是否使用动画
        stellarMap.setGroup(0, true);
        // 设置x,y轴方向上的稀疏度
        stellarMap.setRegularity(5, 10);
    }

    class StellerAdapter implements StellarMap.Adapter {

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
            final TextView tv = new TextView(getActivity());
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
                    Log.e("TAG", "context = " + getContext());//MainActivity
                    Toast.makeText(getContext(), tv.getText(), Toast.LENGTH_SHORT).show();
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

}
