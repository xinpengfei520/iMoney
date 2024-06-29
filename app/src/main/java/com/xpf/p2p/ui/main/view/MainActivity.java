package com.xpf.p2p.ui.main.view;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.xpf.common.base.MvpBaseActivity;
import com.xpf.common.utils.ToastUtil;
import com.xpf.p2p.R;
import com.xpf.p2p.activity.UserInfoActivity;
import com.xpf.p2p.fragment.HomeFragment2;
import com.xpf.p2p.fragment.InvestFragment;
import com.xpf.p2p.fragment.MeFragment;
import com.xpf.p2p.fragment.MoreFragment;
import com.xpf.p2p.ui.main.contract.MainContract;
import com.xpf.p2p.ui.main.presenter.MainPresenter;
import com.xsir.pgyerappupdate.library.PgyerApi;

import me.ele.uetool.UETool;

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:主页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class MainActivity extends MvpBaseActivity<MainContract.IView,
        MainPresenter<MainContract.IView>> implements MainContract.IView {

    ImageView ivBack;
    TextView tvTitle;
    ImageView ivSetting;
    FrameLayout flMain;
    RadioButton rbHome;
    RadioButton rbInvest;
    RadioButton rbAssets;
    RadioButton rbMore;
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
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        flMain = (FrameLayout) findViewById(R.id.fl_main);
        rbHome = (RadioButton) findViewById(R.id.rb_home);
        rbInvest = (RadioButton) findViewById(R.id.rb_invest);
        rbAssets = (RadioButton) findViewById(R.id.rb_assets);
        rbMore = (RadioButton) findViewById(R.id.rb_more);
        rgMain = (RadioGroup) findViewById(R.id.rg_main);

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
            }
        });
        rbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(0);
            }
        });
        rbInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(1);
            }
        });
        rbAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(2);
            }
        });
        rbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(3);
            }
        });

        // 显示 UETool
        UETool.showUETMenu();
        // 显示 UETool 到 y 轴的坐标
        //UETool.showUETMenu(100);
        // 隐藏 UETool
        //UETool.dismissUETMenu();
        handler = new Handler();
        // 默认选中“首页”
        rgMain.check(R.id.rb_home);
        // 选中0视图
        setSelect(0);
        // 检查更新 APP
        PgyerApi.checkUpdate(this);
    }

    @Override
    protected MainPresenter<MainContract.IView> createPresenter() {
        return new MainPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
}
