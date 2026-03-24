package com.xpf.p2p.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import com.alibaba.fastjson.JSON
import com.xpf.p2p.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.xpf.p2p.App
import com.xpf.p2p.constants.ApiRequestUrl
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.databinding.ActivityWelcomeBinding
import com.xpf.p2p.entity.UpdateInfo
import com.xpf.p2p.ui.login.view.LoginActivity
import com.xpf.p2p.ui.main.view.MainActivity
import com.xpf.p2p.utils.AppUtil
import com.xpf.p2p.utils.LogUtils
import com.xpf.p2p.utils.NetStateUtil
import com.xpf.p2p.utils.SpUtil
import com.xpf.p2p.utils.TimeUtil
import com.xpf.p2p.utils.ToastUtil
import com.xpf.p2p.utils.UIUtils
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:欢迎页（闪屏页）
 */
class WelcomeActivity : Activity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var startTime: Long = 0
    private var updateInfo: UpdateInfo? = null
    private var dialog: ProgressDialog? = null
    private var apkFile: File? = null

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_LOGIN -> {
                    val needGuide = SpUtil.getInstance(App.context)
                        .getBoolean(SpKey.IS_NEED_GUIDE, true)
                    if (needGuide) {
                        startActivity(Intent(this@WelcomeActivity, GuideActivity::class.java))
                    } else {
                        startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    }
                    finish()
                }
                WHAT_DOWNLOAD_VERSION_SUCCESS -> {
                    val version = AppUtil.getVersion(this@WelcomeActivity)
                    if (version == updateInfo?.version) {
                        toLoginPager()
                    } else {
                        showDownloadDialog()
                    }
                }
                WHAT_DOWNLOAD_FAIL -> {
                    ToastUtil.show(this@WelcomeActivity, "下载应用文件失败!")
                    toLoginPager()
                }
                WHAT_DOWNLOAD_APK_SUCCESS -> installApk()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        startAnimation()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val versionName = AppUtil.getAppVersionName(this)
        binding.tvVersion.text = "当前版本:V$versionName"
    }

    private fun installApk() {
        dialog?.dismiss()
        finish()
        val intent = Intent("android.intent.action.INSTALL_PACKAGE")
        intent.data = Uri.parse("file:${apkFile?.absolutePath}")
        startActivity(intent)
    }

    private fun showDownloadDialog() {
        AlertDialog.Builder(this)
            .setTitle("发现新版本可用")
            .setMessage(updateInfo?.desc)
            .setCancelable(false)
            .setPositiveButton("下载") { _, _ -> showDownLoad() }
            .setNegativeButton("取消") { _, _ -> toLoginPager() }
            .show()
    }

    @Suppress("DEPRECATION")
    private fun showDownLoad() {
        dialog = ProgressDialog(this).apply {
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setCancelable(false)
            show()
        }

        val filePath = "${getExternalFilesDir(null)}/update_app.apk"
        apkFile = File(filePath)

        Thread {
            try {
                downloadAPK()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            handler.sendEmptyMessage(WHAT_DOWNLOAD_APK_SUCCESS)
        }.start()
    }

    private fun downloadAPK() {
        val fos = FileOutputStream(apkFile)
        val path = updateInfo?.apkUrl
        val url = URL(path)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5000
        conn.readTimeout = 5000
        conn.requestMethod = "GET"
        conn.connect()

        if (conn.responseCode == 200) {
            val inputStream = conn.inputStream
            dialog?.max = conn.contentLength
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
                dialog?.incrementProgressBy(len)
                Thread.sleep(2)
            }
            inputStream.close()
            fos.close()
        } else {
            handler.sendEmptyMessage(WHAT_DOWNLOAD_FAIL)
        }
        conn.disconnect()
    }

    private fun toLoginPager() {
        val currentTimeMillis = System.currentTimeMillis()
        var delayTime = 3000 - (currentTimeMillis - startTime)
        if (delayTime < 0) delayTime = 0
        handler.sendEmptyMessageDelayed(MESSAGE_LOGIN, delayTime)
    }

    private fun updateApp() {
        startTime = System.currentTimeMillis()
        if (!NetStateUtil.isConnected(this)) {
            ToastUtil.show(this, "网络异常!")
            toLoginPager()
        } else {
            val updateUrl = ApiRequestUrl.UPDATE
            RetrofitClient.apiService.post(updateUrl).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val json = response.body()?.string()
                    if (json != null) {
                        LogUtils.d(TAG, json)
                        updateInfo = JSON.parseObject(json, UpdateInfo::class.java)
                        handler.sendEmptyMessage(WHAT_DOWNLOAD_VERSION_SUCCESS)
                    } else {
                        ToastUtil.show(this@WelcomeActivity, "联网获取更新数据失败!")
                        toLoginPager()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    LogUtils.e(TAG, "Throwable:${t.message}")
                    ToastUtil.show(this@WelcomeActivity, "联网获取更新数据失败!")
                    toLoginPager()
                }
            })
        }
    }

    private fun startAnimation() {
        val alphaAnimation = AlphaAnimation(0f, 1f).apply {
            fillAfter = true
            duration = 2000
        }

        val scaleAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            fillAfter = true
            duration = 2000
        }

        val animationSet = AnimationSet(false).apply {
            addAnimation(alphaAnimation)
            addAnimation(scaleAnimation)
            interpolator = DecelerateInterpolator()
        }

        binding.llWelcome.startAnimation(animationSet)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if (TimeUtil.isLoginValid()) {
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    finish()
                } else {
                    handler.sendEmptyMessage(MESSAGE_LOGIN)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        UIUtils.getHandler().removeCallbacksAndMessages(null)
    }

    companion object {
        private const val MESSAGE_LOGIN = 1
        private const val WHAT_DOWNLOAD_VERSION_SUCCESS = 2
        private const val WHAT_DOWNLOAD_FAIL = 3
        private const val WHAT_DOWNLOAD_APK_SUCCESS = 4
        private val TAG = WelcomeActivity::class.java.simpleName
    }
}
