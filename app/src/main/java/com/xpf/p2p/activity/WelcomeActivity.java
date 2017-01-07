package com.xpf.p2p.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xpf.p2p.R;
import com.xpf.p2p.bean.UpdateInfo;
import com.xpf.p2p.common.AppNetConfig;
import com.xpf.p2p.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xpf.p2p.R.id.rl_welcome;

public class WelcomeActivity extends Activity {

    private static final int MESSAGE_MAIN = 1;
    private static final int WHAT_DOWNLOAD_VERSION_SUCCESS = 2;
    private static final int WHAT_DOWNLOAD_FAIL = 3;
    private static final int WHAT_DOWNLOAD_APK_SUCCESS = 4;

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(rl_welcome)
    RelativeLayout rlWelcome;

    private long startTime;
    private UpdateInfo updateInfo;
    private ProgressDialog dialog;
    private File apkFile;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_MAIN:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    break;
                case WHAT_DOWNLOAD_VERSION_SUCCESS: // 获取了服务器端返回的版本信息
                    String version = getVersion();
                    if (version.equals(updateInfo.version)) { // 版本相同
                        toMain();
                    } else {
                        showDownloadDialog();
                    }
                    break;
                case WHAT_DOWNLOAD_FAIL:
                    Toast.makeText(WelcomeActivity.this, "下载应用文件失败", Toast.LENGTH_SHORT).show();
                    toMain();
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

        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        startAnimation(); // 启动动画
        updateApp();      // 联网更新应用的操作
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
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDownLoad();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toMain();
                    }
                }).show();
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

    // 通过发送延迟消息，进入主界面
    private void toMain() {

        finish(); // 进入主页之前销毁欢迎页面
        long currentTimeMillis = System.currentTimeMillis();
        long delayTime = 3000 - (currentTimeMillis - startTime);
        if (delayTime < 0) {
            delayTime = 0;
        }
        // 发送延迟消息
        handler.sendEmptyMessageDelayed(MESSAGE_MAIN, delayTime);
    }

    /**
     * 获取当前应用的版本号
     */
    private String getVersion() {
        String version = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        return version;
    }


    private void updateApp() {

        startTime = System.currentTimeMillis(); // 获取系统当前的时间
        boolean isConnected = isConnected();    // 判断手机是否可以联网

        if (!isConnected) {
            Toast.makeText(WelcomeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            toMain();
        } else {
            String updateUrl = AppNetConfig.UPDATE; // 获取联网请求更新应用的路径
            // 使用AsyncHttpClient实现联网获取版本信息
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(updateUrl, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String json) {
                    // 使用fastjson解析json数据
                    updateInfo = JSON.parseObject(json, UpdateInfo.class);
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_VERSION_SUCCESS);
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    Toast.makeText(WelcomeActivity.this, "联网获取更新数据失败", Toast.LENGTH_SHORT).show();
                    toMain();
                }
            });
        }
    }

    /**
     * 判断手机是否可以联网
     */
    private boolean isConnected() {

        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }
        return connected;
    }

    private void startAnimation() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(2000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2000);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setFillAfter(true);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setInterpolator(new AccelerateInterpolator()); // 使用窜改器设置动画的变化率

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);

        rlWelcome.startAnimation(animationSet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIUtils.getHandler().removeCallbacksAndMessages(null);
    }
}
