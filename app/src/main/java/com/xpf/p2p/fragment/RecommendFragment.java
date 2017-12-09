package com.xpf.p2p.fragment;

import com.loopj.android.http.RequestParams;
import com.xpf.p2p.R;
import com.xpf.p2p.adapter.StellerAdapter;
import com.xpf.p2p.common.BaseFragment;
import com.xpf.p2p.ui.randomLayout.StellarMap;

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
        StellerAdapter stellerAdapter = new StellerAdapter(mContext);
        // 加载显示
        stellarMap.setAdapter(stellerAdapter);
        // 必须提供如下两个方法的调用，否则没有显示效果
        // 设置初始化显示的组别,以及是否使用动画
        stellarMap.setGroup(0, true);
        // 设置x,y轴方向上的稀疏度
        stellarMap.setRegularity(5, 10);
    }

}
