package com.atguigu.p2p.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.p2p.R;
import com.atguigu.p2p.activity.AccountSafeActivity;
import com.atguigu.p2p.activity.BarChartActivity;
import com.atguigu.p2p.activity.ChongZhiActivity;
import com.atguigu.p2p.activity.GestureVerifyActivity;
import com.atguigu.p2p.activity.LineChartActivity;
import com.atguigu.p2p.activity.LoginActivity;
import com.atguigu.p2p.activity.PieChartActivity;
import com.atguigu.p2p.activity.TiXianActivity;
import com.atguigu.p2p.bean.User;
import com.atguigu.p2p.common.BaseActivity;
import com.atguigu.p2p.common.BaseFragment;
import com.atguigu.p2p.utils.BitmapUtils;
import com.atguigu.p2p.utils.UIUtils;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:我的资产页面
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.icon_time)
    RelativeLayout iconTime;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.recharge)
    ImageView recharge;
    @BindView(R.id.withdraw)
    ImageView withdraw;
    @BindView(R.id.ll_touzi)
    TextView llTouzi;
    @BindView(R.id.ll_touzi_zhiguan)
    TextView llTouziZhiguan;
    @BindView(R.id.ll_zichang)
    TextView llZichang;
    @BindView(R.id.ll_zhanquan)
    TextView llZhanquan;

    private SharedPreferences sp;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
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
        sp = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        isLogin(); // 判断是否需要显示需要登录的提示
    }

    private void isLogin() {
        // 在本应用中的sp存储的位置,是否已经保存了用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String userName = sp.getString("UF_ACC", "");
        if (TextUtils.isEmpty(userName)) {
            login();  // 如果没有保存(没有登陆过)就显示AlertDialog
        } else {
            doUser(); // 如果保存了就读取sp中的用户信息,并显示在页面上
        }
    }

    // 未发现登录信息,提示用户登录的Dialog
    private void login() {

        new AlertDialog.Builder(getActivity())
                .setTitle("登录")
                .setMessage("2B请先登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MeFragment.this.getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                        // 跳转到登录页面
                        ((BaseActivity) MeFragment.this.getActivity()).goToActivity(LoginActivity.class, null);
                    }
                })
                .setCancelable(false) //设置按非dialog区域不取消dialog
                .show();
    }

    // 得到了本地的登录信息,加载显示
    private void doUser() {

        // 读取数据,得到内存中的User对象
        User user = ((BaseActivity) this.getActivity()).readUser();
        // 一方面,显示用户名
        textView11.setText(user.UF_ACC);
        // 另一方面,加载显示用户头像
        Picasso.with(getActivity()).load(user.UF_AVATAR_URL).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                // 对Bitmap进行压缩处理
                Bitmap zoom = BitmapUtils.zoom(source, UIUtils.dp2px(62), UIUtils.dp2px(62));
                // 对Bitmap进行圆形处理
                Bitmap circleBitmap = BitmapUtils.circleBitmap(zoom);
                source.recycle(); // 回收,否则会出现内存泄漏

                return circleBitmap;
            }

            @Override
            public String key() {
                return ""; // 此方法不能返回null否则报异常
            }
        }).into(imageView1);

        // 如果在本地发现了用户设置了手势密码,则在此需要验证
        boolean isOpen = sp.getBoolean("isOpen", false);
        if (isOpen) {
            ((BaseActivity) this.getActivity()).goToActivity(GestureVerifyActivity.class, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String filePath = this.getActivity().getCacheDir() + "/tx.png";
        File file = new File(filePath);
        if (file.exists()) { // 如果存在
            // 存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imageView1.setImageBitmap(bitmap);
        }
    }

    @OnClick({R.id.recharge, R.id.withdraw, R.id.ll_touzi, R.id.ll_touzi_zhiguan, R.id.ll_zichang, R.id.ll_zhanquan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge:
                ((BaseActivity) this.getActivity()).goToActivity(ChongZhiActivity.class, null);
                break;
            case R.id.withdraw:
                ((BaseActivity) this.getActivity()).goToActivity(TiXianActivity.class, null);
                break;
            case R.id.ll_touzi:
                ((BaseActivity) this.getActivity()).goToActivity(LineChartActivity.class, null);
                break;
            case R.id.ll_touzi_zhiguan:
                ((BaseActivity) this.getActivity()).goToActivity(BarChartActivity.class, null);
                break;
            case R.id.ll_zichang:
                ((BaseActivity) this.getActivity()).goToActivity(PieChartActivity.class, null);
                break;
            case R.id.ll_zhanquan:
                ((BaseActivity) this.getActivity()).goToActivity(AccountSafeActivity.class, null);
                break;
        }
    }
}
