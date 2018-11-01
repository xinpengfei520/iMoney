package com.xpf.p2p.ui.main.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xpf.common.base.MvpBaseActivity;
import com.xpf.common.utils.ToastUtil;
import com.xpf.p2p.R;
import com.xpf.p2p.activity.UserInfoActivity;
import com.xpf.p2p.fragment.HomeFragment2;
import com.xpf.p2p.fragment.InvestFragment;
import com.xpf.p2p.fragment.MeFragment;
import com.xpf.p2p.fragment.MoreFragment;
import com.xpf.p2p.service.DownLoadService;
import com.xpf.p2p.ui.main.contract.MainContract;
import com.xpf.p2p.ui.main.presenter.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:主页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class MainActivity extends MvpBaseActivity<MainContract.IView,
        MainPresenter<MainContract.IView>> implements MainContract.IView, EasyPermissions.PermissionCallbacks {

    private static final int RC_UPLOAD_PERM = 0x224;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_invest)
    RadioButton rbInvest;
    @BindView(R.id.rb_assets)
    RadioButton rbAssets;
    @BindView(R.id.rb_more)
    RadioButton rbMore;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;

    private HomeFragment2 homeFragment;
    private InvestFragment investFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;
    private FragmentTransaction transaction;

    private boolean isFlag = true;
    private Handler handler;
    private String mDownloadUrl;

    @Override
    protected void initData() {
        handler = new Handler();
        // 默认选中“首页”
        rgMain.check(R.id.rb_home);
        // 选中0视图
        setSelect(0);
        // 检查更新 APP
        mPresenter.checkUpdate();
    }

    @Override
    protected MainPresenter<MainContract.IView> createPresenter() {
        return new MainPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.iv_back, R.id.iv_setting, R.id.rb_home, R.id.rb_invest, R.id.rb_assets, R.id.rb_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_setting:
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                break;
            case R.id.rb_home:
                setSelect(0);
                break;
            case R.id.rb_invest:
                setSelect(1);
                break;
            case R.id.rb_assets:
                setSelect(2);
                break;
            case R.id.rb_more:
                setSelect(3);
                break;
        }
    }

    /**
     * 提供相应的fragment的显示
     * 如何在activity中动态加载fragment
     */
    private void setSelect(int i) {
        // 此处的transaction不能为全局变量（只能提交一次），否则会报java.lang.IllegalStateException: commit already called
        transaction = this.getSupportFragmentManager().beginTransaction();
        hideFragment();
        switch (i) {
            case 0:
                tvTitle.setText(getString(R.string.rb_home));
                ivBack.setVisibility(View.INVISIBLE);
                ivSetting.setVisibility(View.INVISIBLE);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment2();
                    transaction.add(R.id.fl_main, homeFragment);
                }
                transaction.show(homeFragment);
                break;
            case 1:
                tvTitle.setText(getString(R.string.rb_invest));
                ivSetting.setVisibility(View.INVISIBLE);
                ivBack.setVisibility(View.INVISIBLE);
                if (investFragment == null) {
                    investFragment = new InvestFragment();
                    transaction.add(R.id.fl_main, investFragment);
                }
                transaction.show(investFragment);
                break;
            case 2:
                tvTitle.setText(getString(R.string.rb_my_assets));
                ivSetting.setVisibility(View.VISIBLE);
                ivBack.setVisibility(View.INVISIBLE);
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                transaction.show(meFragment);
                break;
            case 3:
                tvTitle.setText(getString(R.string.rb_more));
                ivSetting.setVisibility(View.INVISIBLE);
                ivBack.setVisibility(View.INVISIBLE);
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.fl_main, moreFragment);
                }
                transaction.show(moreFragment);
                break;
            default:
                break;
        }
        transaction.commit();// 提交事务
    }

    /**
     * 隐藏所有的Fragment的显示
     */
    private void hideFragment() {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (investFragment != null) {
            transaction.hide(investFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isFlag) {
            isFlag = false;
            ToastUtil.show(this, getString(R.string.toast_exit_click_again));
            handler.postDelayed(() -> {
                isFlag = true; // 在2s时,恢复isFlag的变量值
            }, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            // 保证在activity退出前,移除所有未被执行的消息和回调方法,避免出现内存泄漏!
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onUpdateResult(String url, String description) {
        this.mDownloadUrl = url;
        new AlertDialog.Builder(this)
                .setTitle("有新版本了")
                .setMessage(description)
                .setPositiveButton("更新", (dialogInterface, i) -> downLoad())
                .setNegativeButton("暂不更新", null)
                .show();
    }

    public void downLoad() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
                startDownloadService();
            } else {
                EasyPermissions.requestPermissions(this, "需要读取存储和安装的权限",
                        RC_UPLOAD_PERM, PERMISSIONS);
            }
        } else {
            startDownloadService();
        }
    }

    private void startDownloadService() {
        String apkName = "iMoney_android_app";
        DownLoadService.startAction(MainActivity.this, mDownloadUrl, apkName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case RC_UPLOAD_PERM:
                startDownloadService();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}