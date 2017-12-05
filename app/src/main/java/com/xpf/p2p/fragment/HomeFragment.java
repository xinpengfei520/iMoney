package com.xpf.p2p.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.xpf.p2p.R;
import com.xpf.p2p.bean.Image;
import com.xpf.p2p.bean.Index;
import com.xpf.p2p.bean.Product;
import com.xpf.p2p.common.AppNetConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.tv_home_rate)
    TextView tvHomeRate;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    //    @BindView(R.id.cp_home)
//    CirclePageIndicator cpHome;
    private Index index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        initData();  // 初始化页面数据
        return view;
    }

    private void initData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppNetConfig.INDEX, new AsyncHttpResponseHandler() {

            // 成功的获取响应数据
            @Override
            public void onSuccess(String content) {

                // 1.使用fastJson解析数据,并封装数据到java对象中
                JSONObject jsonObject = JSON.parseObject(content);
                String proInfo = jsonObject.getString("proInfo");
                Product product = JSON.parseObject(proInfo, Product.class);

                String imageArr = jsonObject.getString("imageArr");
                List<Image> images = JSON.parseArray(imageArr, Image.class);

                index = new Index();
                index.product = product;
                index.images = images;

                // 2.设置ViewPager,加载显示图片
                viewpager.setAdapter(new MyPagerAdapter());
//                cpHome.setViewPager(viewpager);

                //3.根据得到的产品的数据，更新界面中的产品展示
                String yearRate = index.product.yearRate;
                tvHomeRate.setText(yearRate + "%");
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Toast.makeText(getActivity(), "获取服务器数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return index.images == null ? 0 : index.images.size();
        }

        // 判断视图是否由集合中的数据创建
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 返回一个具体的item对应的视图对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 加载具体的图片
            String imaurl = index.images.get(position).IMAURL;
            Picasso.with(getActivity()).load(imaurl).into(imageView);
            // 添加到当前的container中
            container.addView(imageView);

            return imageView;
        }

        // 销毁指定的视图对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
