package com.xpf.p2p.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.xpf.common.base.BaseActivity;
import com.xpf.p2p.R;
import com.xpf.p2p.utils.pay.PayKeys;
import com.xpf.p2p.utils.pay.PayResult;
import com.xpf.p2p.utils.pay.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vance on 2016/8/3 :)
 * Function:用户账户充值页面
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class ChongZhiActivity extends BaseActivity {

    private static final String TAG = "ChongZhiActivity";
    private static final String PARTNER = PayKeys.DEFAULT_PARTNER;
    private static final String SELLER = PayKeys.DEFAULT_SELLER;
    private static final String RSA_PRIVATE = PayKeys.PRIVATE;
    private static final int SDK_PAY_FLAG = 1;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.chongzhi_text)
    TextView chongzhiText;
    @BindView(R.id.chongzhi_et)
    EditText chongzhiEt;
    @BindView(R.id.chongzhi_text2)
    TextView chongzhiText2;
    @BindView(R.id.yue_tv)
    TextView yueTv;
    @BindView(R.id.chongzhi_btn)
    Button chongzhiBtn;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签,建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功,具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ChongZhiActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ChongZhiActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ChongZhiActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void initData() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("充值");
        ivSetting.setVisibility(View.GONE);
        chongzhiBtn.setClickable(false); // 默认充值按钮不可点击

        // 给EditText设置文本内容变化的监听
        chongzhiEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("TAG", "ChongZhiActivity----> beforeTextChanged()");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TAG", "ChongZhiActivity----> onTextChanged()");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TAG", "ChongZhiActivity----> afterTextChanged()");
                String moneyNum = chongzhiEt.getText().toString().trim();
                if (TextUtils.isEmpty(moneyNum)) { // 输入框内容为空
                    // 1.设置Button的背景为灰色
                    chongzhiBtn.setBackgroundResource(R.drawable.btn_023);
                    // 2.设置Button不可点击
                    chongzhiBtn.setClickable(false);

                } else {
                    // 1.设置Button的背景为浅蓝色
                    chongzhiBtn.setBackgroundResource(R.drawable.btn_01);
                    // 2.设置Button可点击
                    chongzhiBtn.setClickable(true);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chong_zhi;
    }

    @OnClick({R.id.iv_back, R.id.chongzhi_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                removeCurrentActivity(); // 销毁当前的activity
                break;
            case R.id.chongzhi_btn:
                recharge(); // 充值
                break;
        }
    }

    private void recharge() {
        // 订单
        String orderInfo = getOrderInfo("iphone 7 plus 256G", "史上最强配置iphone", "0.01");

        // 对订单做RSA签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask对象
                PayTask alipay = new PayTask(ChongZhiActivity.this);
                // 调用支付接口,获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order.
     * 生成商户订单号，该值在商户端应保持唯一(可自定义格式规范)
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();//next(32)
        key = key.substring(0, 15);

        return key;
    }

}
