package com.xpf.p2p.activity;

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

import com.xpf.common.base.BaseActivity;
import com.xpf.common.utils.UIUtils;
import com.xpf.p2p.R;

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:提现页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class TiXianActivity extends BaseActivity {

    ImageView ivBack;
    TextView tvTitle;
    ImageView ivSetting;
    TextView accountZhifubao;
    RelativeLayout selectBank;
    TextView chongzhiText;
    View view;
    EditText inputMoney;
    TextView chongzhiText2;
    TextView textView5;
    Button btnTixian;

    @Override
    protected void initData() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        accountZhifubao = (TextView) findViewById(R.id.account_zhifubao);
        selectBank = (RelativeLayout) findViewById(R.id.select_bank);
        chongzhiText = (TextView) findViewById(R.id.chongzhi_text);
        view = (View) findViewById(R.id.view);
        inputMoney = (EditText) findViewById(R.id.input_money);
        chongzhiText2 = (TextView) findViewById(R.id.chongzhi_text2);
        textView5 = (TextView) findViewById(R.id.textView5);
        btnTixian = (Button) findViewById(R.id.btn_tixian);

        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("提现");
        ivSetting.setVisibility(View.GONE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCurrentActivity(); // 结束当前的activity
            }
        });
        btnTixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw(); // 提现
            }
        });

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
