package com.atguigu.p2p.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.atguigu.p2p.R;
import com.atguigu.p2p.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账户安全页面
 */
public class AccountSafeActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.togglebutton)
    ToggleButton togglebutton;
    @BindView(R.id.btn_resetGesture)
    Button btnResetGesture;
    @BindView(R.id.activity_account_safe)
    LinearLayout activityAccountSafe;

    private SharedPreferences sp;

    @Override
    protected void initData() {
        ivBack.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.GONE);
        tvTitle.setText("账户安全设置");

        sp = this.getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        // 读取当前的toggleButton的状态并显示
        boolean isOpen = sp.getBoolean("isOpen", false);
        togglebutton.setChecked(isOpen);
        btnResetGesture.setEnabled(isOpen);//设置button的可操作性

        togglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // 判断本地是否设置过手势密码
                    String inputCode = sp.getString("inputCode", "");
                    if (TextUtils.isEmpty(inputCode)) {

                        Toast.makeText(AccountSafeActivity.this, "开启手势解锁", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(AccountSafeActivity.this)
                                .setTitle("是否现在设置手势密码")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String inputCode1 = sp.getString("inputCode", "");
                                        if (TextUtils.isEmpty(inputCode1)) { // 用户之前没有设置过
                                            Toast.makeText(AccountSafeActivity.this, "现在设置手势密码", Toast.LENGTH_SHORT).show();
                                            // toggleButton.setChecked(true);
                                            btnResetGesture.setEnabled(true);
                                            sp.edit().putBoolean("isOpen", true).commit();
                                            goToActivity(GestureEditActivity.class, null);
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(AccountSafeActivity.this, "取消设置手势密码", Toast.LENGTH_SHORT).show();
                                        togglebutton.setChecked(false);
                                        btnResetGesture.setEnabled(false);
                                        sp.edit().putBoolean("isOpen", false).commit();
                                    }
                                }).show();
                    } else { // 之前设置过
                        Toast.makeText(AccountSafeActivity.this, "开启了手势密码", Toast.LENGTH_SHORT).show();
                        // toggleButton.setChecked(true);
                        btnResetGesture.setEnabled(true);
                        sp.edit().putBoolean("isOpen", true).commit();
                    }

                } else {
                    Toast.makeText(AccountSafeActivity.this, "关闭手势解锁", Toast.LENGTH_SHORT).show();
                    // toggleButton.setChecked(false);
                    sp.edit().putBoolean("isOpen", false).commit();
                    btnResetGesture.setEnabled(false);
                }
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_safe;
    }

    @OnClick({R.id.iv_back, R.id.togglebutton, R.id.btn_resetGesture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                removeCurrentActivity();
                break;
            case R.id.togglebutton:
                break;
            case R.id.btn_resetGesture:
                // 给页面中的“button”设置重置密码的点击事件
                goToActivity(GestureEditActivity.class, null);
                break;
        }
    }
}
