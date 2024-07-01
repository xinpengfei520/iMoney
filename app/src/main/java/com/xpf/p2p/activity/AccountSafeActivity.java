package com.xpf.p2p.activity;

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

import com.xpf.p2p.R;
import com.xpf.p2p.base.BaseActivity;

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:账户安全页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class AccountSafeActivity extends BaseActivity {

    ImageView ivBack;
    TextView tvTitle;
    ImageView ivSetting;
    ToggleButton togglebutton;
    Button btnResetGesture;
    LinearLayout activityAccountSafe;

    private SharedPreferences sp;

    @Override
    protected void initData() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        togglebutton = (ToggleButton) findViewById(R.id.togglebutton);
        btnResetGesture = (Button) findViewById(R.id.btn_resetGesture);
        activityAccountSafe = (LinearLayout) findViewById(R.id.activity_account_safe);
        ivBack.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.GONE);
        tvTitle.setText("账户安全设置");

        ivBack.setOnClickListener(v -> removeCurrentActivity());
        btnResetGesture.setOnClickListener(v -> {
            // 给页面中的“button”设置重置密码的点击事件
            goToActivity(GestureEditActivity.class, null);
        });

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
}
