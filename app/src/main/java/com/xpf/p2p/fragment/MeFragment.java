package com.xpf.p2p.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.xpf.p2p.R;
import com.xpf.p2p.activity.ChongZhiActivity;
import com.xpf.p2p.activity.GestureVerifyActivity;
import com.xpf.p2p.activity.TiXianActivity;
import com.xpf.p2p.base.BaseActivity;
import com.xpf.p2p.base.BaseFragment;
import com.xpf.p2p.base.MvpBaseActivity;
import com.xpf.p2p.constants.SpKey;
import com.xpf.p2p.entity.User;
import com.xpf.p2p.ui.login.view.LoginActivity;
import com.xpf.p2p.utils.BitmapUtils;
import com.xpf.p2p.utils.SpUtil;
import com.xpf.p2p.utils.TimeUtil;
import com.xpf.p2p.utils.UIUtils;
import com.xpf.p2p.utils.UserInfoUtils;

import java.io.File;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:我的资产页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class MeFragment extends BaseFragment {

    ImageView imageView1;
    RelativeLayout iconTime;
    TextView textView11;
    ImageView recharge;
    ImageView withdraw;
    TextView llTouzi;
    TextView llTouziZhiguan;
    TextView llZichang;
    TextView llZhanquan;
    private static final String TAG = MeFragment.class.getSimpleName();

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
        imageView1 = mView.findViewById(R.id.imageView1);
        iconTime = mView.findViewById(R.id.icon_time);
        textView11 = mView.findViewById(R.id.textView11);
        recharge = mView.findViewById(R.id.recharge);
        withdraw = mView.findViewById(R.id.withdraw);
        llTouzi = mView.findViewById(R.id.ll_touzi);
        llTouziZhiguan = mView.findViewById(R.id.ll_touzi_zhiguan);
        llZichang = mView.findViewById(R.id.ll_zichang);
        llZhanquan = mView.findViewById(R.id.ll_zhanquan);

        recharge.setOnClickListener(v -> {
            // 跳转到充值页面
            ((BaseActivity) getActivity()).goToActivity(ChongZhiActivity.class, null);
        });
        withdraw.setOnClickListener(v -> {
            // 跳转到提现页面
            ((BaseActivity) getActivity()).goToActivity(TiXianActivity.class, null);
        });
        llTouzi.setOnClickListener(v -> {
            // 跳转到充值页面
            ((BaseActivity) getActivity()).goToActivity(ChongZhiActivity.class, null);
        });
        llTouziZhiguan.setOnClickListener(v -> {
            // 跳转到充值页面
            ((BaseActivity) getActivity()).goToActivity(ChongZhiActivity.class, null);
        });
        llZichang.setOnClickListener(v -> {
            // 跳转到充值页面
            ((BaseActivity) getActivity()).goToActivity(ChongZhiActivity.class, null);
        });
        llZhanquan.setOnClickListener(v -> {
            // 跳转到充值页面
            ((BaseActivity) getActivity()).goToActivity(ChongZhiActivity.class, null);
        });

        isLogin(); // 判断是否需要显示需要登录的提示
    }

    private void isLogin() {
        // 在本应用中的sp存储的位置,是否已经保存了用户的登录信息
        if (TimeUtil.isLoginValid()) {
            doUser(); // 如果保存了就读取sp中的用户信息,并显示在页面上
        } else {
            login();  // 如果没有保存(没有登陆过)就显示AlertDialog
        }
    }

    // 未发现登录信息,提示用户登录的Dialog
    private void login() {
        new AlertDialog.Builder(getActivity())
                .setTitle("登录")
                .setMessage("请先登录！")
                .setPositiveButton("确定", (dialog, which) -> {
                    Toast.makeText(MeFragment.this.getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    // 跳转到登录页面
                    //((BaseActivity) MeFragment.this.getActivity()).goToActivity(LoginActivity.class, null);
                    LoginActivity.actionStart(mContext);
                })
                .setCancelable(false) //设置按非dialog区域不取消dialog
                .show();
    }

    // 得到了本地的登录信息,加载显示
    private void doUser() {
        // 读取数据,得到内存中的User对象
        User user = UserInfoUtils.readUser(getContext());
        if (user == null) {
            return;
        }
        // 一方面,显示用户名
        if (!TextUtils.isEmpty(user.UF_ACC)) {
            textView11.setText(user.UF_ACC);
        }

        if (!TextUtils.isEmpty(user.UF_AVATAR_URL)) {
            // 另一方面,加载显示用户头像
            Picasso.get().load(user.UF_AVATAR_URL).transform(new Transformation() {
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
        }

        // 如果在本地发现了用户设置了手势密码,则在此需要验证
        boolean isOpen = SpUtil.getInstance(mContext).getBoolean(SpKey.GESTURE_IS_OPEN, false);
        if (isOpen) {
            if (this.getActivity() instanceof MvpBaseActivity) {
                ((MvpBaseActivity<?, ?>) this.getActivity()).goToActivity(GestureVerifyActivity.class, null);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserPhoto();
    }

    private void setUserPhoto() {
        String filePath = this.getActivity().getCacheDir() + "/tx.png";
        File file = new File(filePath);
        if (file.exists()) { // 如果存在
            // 存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imageView1.setImageBitmap(bitmap);
        }
    }
}
