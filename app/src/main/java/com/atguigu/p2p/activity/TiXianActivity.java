package com.atguigu.p2p.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.p2p.R;
import com.atguigu.p2p.common.BaseActivity;
import com.atguigu.p2p.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TiXianActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.account_zhifubao)
    TextView accountZhifubao;
    @BindView(R.id.select_bank)
    RelativeLayout selectBank;
    @BindView(R.id.chongzhi_text)
    TextView chongzhiText;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.input_money)
    EditText inputMoney;
    @BindView(R.id.chongzhi_text2)
    TextView chongzhiText2;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.btn_tixian)
    Button btnTixian;

    @Override
    protected void initData() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("提现");
        ivSetting.setVisibility(View.GONE);

        btnTixian.setClickable(false);
        //给 EditText设置文本内容变化的监听
        inputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String moneyNum = inputMoney.getText().toString().trim();
                if (TextUtils.isEmpty(moneyNum)) {//空
                    // 1.设置Button的背景颜色
                    btnTixian.setBackgroundResource(R.drawable.btn_023);
                    // 2.设置Button可点击性
                    btnTixian.setClickable(false);

                } else {
                    // 1.设置Button的背景颜色
                    btnTixian.setBackgroundResource(R.drawable.btn_01);
                    // 2.设置Button可点击性
                    btnTixian.setClickable(true);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ti_xian;
    }

    @OnClick({R.id.iv_back, R.id.btn_tixian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                removeCurrentActivity(); // 结束当前的activity
                break;
            case R.id.btn_tixian:
                withdraw(); // 提现
                break;
        }
    }

    private void withdraw() {
        String inputMoney = this.inputMoney.getText().toString();
        // 请求服务器，将提现的数额发给指定的servlet下处理。略
        Toast.makeText(TiXianActivity.this, "您的提现请求发送成功,请您稍后查询到账信息", Toast.LENGTH_SHORT).show();

        // 页面显示2秒以后，自动关闭
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeCurrentActivity();
            }
        }, 2000);
    }
}
