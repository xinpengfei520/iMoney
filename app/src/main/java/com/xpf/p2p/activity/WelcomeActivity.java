package com.xpf.p2p.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xpf.p2p.App;
import com.xpf.p2p.R;
import com.xpf.p2p.constants.ApiRequestUrl;
import com.xpf.p2p.constants.SpKey;
import com.xpf.p2p.entity.UpdateInfo;
import com.xpf.p2p.ui.login.view.LoginActivity;
import com.xpf.p2p.ui.main.view.MainActivity;
import com.xpf.p2p.utils.AppUtil;
import com.xpf.p2p.utils.LogUtils;
import com.xpf.p2p.utils.NetStateUtil;
import com.xpf.p2p.utils.SpUtil;
import com.xpf.p2p.utils.TimeUtil;
import com.xpf.p2p.utils.ToastUtil;
import com.xpf.p2p.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by x-sir on 2016/8/3 :)
 * Function:欢迎页（闪屏页）
 * {@link # https://github.com/xinpengfei520/P2P}
 */
public class WelcomeActivity extends Activity {

    private static final int MESSAGE_LOGIN = 1;
    private static final int WHAT_DOWNLOAD_VERSION_SUCCESS = 2;
    private static final int WHAT_DOWNLOAD_FAIL = 3;
    private static final int WHAT_DOWNLOAD_APK_SUCCESS = 4;
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    private TextView tvVersion;
    private LinearLayout llWelcome;

    private long startTime;
    private UpdateInfo updateInfo;
    private ProgressDialog dialog;
    private File apkFile;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LOGIN:
                    boolean aBoolean = SpUtil.getInstance(App.Companion.getContext())
                            .getBoolean(SpKey.IS_NEED_GUIDE, true);
                    if (aBoolean) {
                        startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    }
                    finish(); // 进入主页之前销毁欢迎页面
                    break;
                case WHAT_DOWNLOAD_VERSION_SUCCESS: // 获取了服务器端返回的版本信息
                    String version = AppUtil.getVersion(WelcomeActivity.this);
                    if (version.equals(updateInfo.version)) { // 版本相同
                        toLoginPager();
                    } else {
                        showDownloadDialog();
                    }
                    break;
                case WHAT_DOWNLOAD_FAIL:
                    ToastUtil.show(WelcomeActivity.this, "下载应用文件失败!");
                    toLoginPager();
                    break;
                case WHAT_DOWNLOAD_APK_SUCCESS:
                    installApk();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉窗口标题和状态栏设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        tvVersion = findViewById(R.id.tv_version);
        llWelcome = findViewById(R.id.llWelcome);
        initData();
        startAnimation(); // 启动动画
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        String versionName = AppUtil.getAppVersionName(this);
        tvVersion.setText("当前版本:V" + versionName);
    }

    /**
     * 安装apk更新包
     */
    private void installApk() {
        dialog.dismiss();
        finish();
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
        startActivity(intent);
    }

    /**
     * 显示是否需要联网下载最新版本apk的Dialog
     */
    private void showDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("发现新版本可用")
                .setMessage(updateInfo.desc)
                .setCancelable(false)
                .setPositiveButton("下载", (dialog, which) -> showDownLoad())
                .setNegativeButton("取消", (dialog, which) -> toLoginPager()).show();
    }

    /**
     * 联网下载指定url地址对应的apk文件
     * 1.提供ProgressDialog
     * 2.提供本地的存储文件
     * 3.联网下载数据
     * 4.安装
     */
    private void showDownLoad() {
        // 1.提供ProgressDialog
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();

        // 2.提供本地的存储文件:sd卡路径1
        String filePath = this.getExternalFilesDir(null) + "/update_app.apk";
        apkFile = new File(filePath);
        // 3.联网下载数据
        new Thread() {
            public void run() {
                try {
                    downloadAPK();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(WHAT_DOWNLOAD_APK_SUCCESS);
            }
        }.start();
    }

    /**
     * 下载对应url下的apk文件
     */
    private void downloadAPK() throws Exception {
        FileOutputStream fos = new FileOutputStream(apkFile);
        String path = updateInfo.apkUrl;
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        conn.connect();

        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            dialog.setMax(conn.getContentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                dialog.incrementProgressBy(len);
                Thread.sleep(2);
            }
            // 暂且使用throws的方式处理异常了
            is.close();
            fos.close();
        } else {
            handler.sendEmptyMessage(WHAT_DOWNLOAD_FAIL);
        }
        // 关闭连接
        conn.disconnect();
    }

    // 通过发送延迟消息，进入登录界面
    private void toLoginPager() {
        long currentTimeMillis = System.currentTimeMillis();
        long delayTime = 3000 - (currentTimeMillis - startTime);
        if (delayTime < 0) {
            delayTime = 0;
        }
        // 发送延迟消息
        handler.sendEmptyMessageDelayed(MESSAGE_LOGIN, delayTime);
    }

    /**
     * 更新APP
     */
    private void updateApp() {
        startTime = System.currentTimeMillis(); // 获取系统当前的时间
        // 判断手机是否可以联网
        if (!NetStateUtil.isConnected(this)) {
            ToastUtil.show(WelcomeActivity.this, "网络异常!");
            toLoginPager();
        } else {
            String updateUrl = ApiRequestUrl.UPDATE; // 获取联网请求更新应用的路径
            // 使用AsyncHttpClient实现联网获取版本信息
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(updateUrl, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String json) {
                    LogUtils.d(TAG, json);
                    // 使用fastJson解析json数据
                    updateInfo = JSON.parseObject(json, UpdateInfo.class);
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_VERSION_SUCCESS);
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    LogUtils.e(TAG, "Throwable:" + error.getMessage() + ",content:" + content);
                    ToastUtil.show(WelcomeActivity.this, "联网获取更新数据失败!");
                    toLoginPager();
                }
            });
        }
    }

    private void startAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(2000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2000);

//        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        alphaAnimation.setFillAfter(true);
//        rotateAnimation.setDuration(2000);
//        rotateAnimation.setInterpolator(new AccelerateInterpolator()); // 使用窜改器设置动画的变化率

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
//        animationSet.addAnimation(rotateAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());

        llWelcome.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (TimeUtil.isLoginValid()) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                } else {
                    // 联网更新应用的操作
                    //updateApp();
                    // 跳转到登录页面
                    handler.sendEmptyMessage(MESSAGE_LOGIN);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        UIUtils.getHandler().removeCallbacksAndMessages(null);
    }
}
