package com.xpf.p2p.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.xpf.common.base.BaseActivity;
import com.xpf.p2p.R;

import java.util.ArrayList;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:折线图页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class LineChartActivity extends BaseActivity {

    ImageView ivBack;
    TextView tvTitle;
    ImageView ivSetting;
    LineChart lineChart;

    private Typeface mTf;

    @Override
    protected void initData() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("折线图");
        ivSetting.setVisibility(View.GONE);
        ivBack.setOnClickListener(v -> removeCurrentActivity());

        // 加载本地的字体库
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // 设置图表的描述
        lineChart.setDescription("我的资产的变化情况");
        // 是否设置网格背景
        lineChart.setDrawGridBackground(false);

        // 获取图表的x轴
        XAxis xAxis = lineChart.getXAxis();
        // 设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置x轴的字体
        xAxis.setTypeface(mTf);
        // 是否绘制x轴网格线
        xAxis.setDrawGridLines(false);
        // 是否绘制x轴轴线
        xAxis.setDrawAxisLine(true);

        // 设置Y轴
        YAxis leftAxis = lineChart.getAxisLeft();
        // 设置Y轴的子体
        leftAxis.setTypeface(mTf);
        // 参数1:设置左边区间的个数,参数2:是否需要均匀分布：false均匀
        leftAxis.setLabelCount(5, false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        // 设置右边为不显示状态
        rightAxis.setEnabled(false);

        // set data
        lineChart.setData(generateDataLine());

        // do not forget to refresh the chart
        // lineChart.invalidate();
        lineChart.animateX(750); // 设置x轴方向的动画
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_line_chart;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        // 定义y轴显示的数据(随机生成),真实项目数据来自服务器
        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        // 获取折线1的数据集
        LineDataSet d1 = new LineDataSet(e1, "New DataSet 1");
        // 设置折线的宽度
        d1.setLineWidth(2.5f);
        // 设置圆圈的半径
        d1.setCircleSize(4.5f);
        // 设置选中圆圈时x,y轴线高亮的颜色
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        // 设置小圆圈是否显示数据
        d1.setDrawValues(false);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }
}
