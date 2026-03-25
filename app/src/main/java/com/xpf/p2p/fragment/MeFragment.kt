package com.xpf.p2p.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.xpf.p2p.R
import com.xpf.p2p.activity.ChongZhiActivity
import com.xpf.p2p.activity.GestureVerifyActivity
import com.xpf.p2p.activity.TiXianActivity
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.base.MvpBaseActivity
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.databinding.FragmentMeBinding
import com.xpf.p2p.entity.User
import com.xpf.p2p.utils.BitmapUtils
import com.xpf.p2p.utils.LoginDialogUtils
import com.xpf.p2p.utils.SpUtil
import com.xpf.p2p.utils.TimeUtil
import com.xpf.p2p.utils.UIUtils
import com.xpf.p2p.utils.UserInfoUtils
import java.io.File

/**
 * Created by xpf on 2016/11/11 :)
 * Function:我的资产页面
 */
class MeFragment : BaseFragment() {

    private var _binding: FragmentMeBinding? = null
    private val binding get() = _binding!!

    override fun getLayoutId(): Int = R.layout.fragment_me

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String>? = null

    override fun initData(content: String?) {
        _binding = FragmentMeBinding.bind(mView!!)

        binding.meContent.recharge.setOnClickListener {
            if (LoginDialogUtils.checkLoginOrPrompt(activity)) {
                (activity as? BaseActivity<*>)?.goToActivity(ChongZhiActivity::class.java, null)
            }
        }
        binding.meContent.withdraw.setOnClickListener {
            if (LoginDialogUtils.checkLoginOrPrompt(activity)) {
                (activity as? BaseActivity<*>)?.goToActivity(TiXianActivity::class.java, null)
            }
        }
        binding.meContent.llTouzi.setOnClickListener {
            if (LoginDialogUtils.checkLoginOrPrompt(activity)) {
                (activity as? BaseActivity<*>)?.goToActivity(ChongZhiActivity::class.java, null)
            }
        }
        binding.meContent.llTouziZhiguan.setOnClickListener {
            if (LoginDialogUtils.checkLoginOrPrompt(activity)) {
                (activity as? BaseActivity<*>)?.goToActivity(ChongZhiActivity::class.java, null)
            }
        }
        binding.meContent.llZichang.setOnClickListener {
            if (LoginDialogUtils.checkLoginOrPrompt(activity)) {
                (activity as? BaseActivity<*>)?.goToActivity(ChongZhiActivity::class.java, null)
            }
        }
        binding.meContent.llZhanquan.setOnClickListener {
            if (LoginDialogUtils.checkLoginOrPrompt(activity)) {
                (activity as? BaseActivity<*>)?.goToActivity(ChongZhiActivity::class.java, null)
            }
        }

        // 如果已登录则加载用户信息，不弹框
        if (TimeUtil.isLoginValid()) {
            doUser()
        }
    }

    private fun doUser() {
        val user: User = UserInfoUtils.readUser(context) ?: return

        if (!TextUtils.isEmpty(user.UF_ACC)) {
            binding.meContent.textView11.text = user.UF_ACC
        }

        if (!TextUtils.isEmpty(user.UF_AVATAR_URL)) {
            Picasso.get().load(user.UF_AVATAR_URL).transform(object : Transformation {
                override fun transform(source: Bitmap): Bitmap {
                    val zoom = BitmapUtils.zoom(source, UIUtils.dp2px(62).toFloat(), UIUtils.dp2px(62).toFloat())
                    val circleBitmap = BitmapUtils.circleBitmap(zoom!!)
                    source.recycle()
                    return circleBitmap
                }

                override fun key(): String = ""
            }).into(binding.meContent.imageView1)
        }

        val isOpen = SpUtil.getInstance(mContext).getBoolean(SpKey.GESTURE_IS_OPEN, false)
        if (isOpen) {
            (activity as? MvpBaseActivity<*, *, *>)?.goToActivity(GestureVerifyActivity::class.java, null)
        }
    }

    override fun onResume() {
        super.onResume()
        setUserPhoto()
    }

    private fun setUserPhoto() {
        val filePath = "${context?.cacheDir}/tx.png"
        val file = File(filePath)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(filePath)
            binding.meContent.imageView1.setImageBitmap(bitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = MeFragment::class.java.simpleName
    }
}
