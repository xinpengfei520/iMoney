package com.xpf.p2p.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.databinding.FragmentMoreBinding
import com.xpf.p2p.ui.multilanguage.view.MultiLanguageActivity
import com.xpf.p2p.utils.AppUtil

/**
 * Created by xpf on 2016/11/11 :)
 * Function:更多页面
 */
class MoreFragment : BaseFragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String>? = null

    @SuppressLint("SetTextI18n")
    override fun initData(content: String?) {
        _binding = FragmentMoreBinding.bind(mView!!)

        // 版本号
        binding.tvVersion.text = "V${AppUtil.getAppVersionName(activity!!)}"

        // 多语言
        binding.itemLanguage.setOnClickListener {
            startActivity(Intent(mContext, MultiLanguageActivity::class.java))
        }

        // 消息通知
        binding.itemNotification.setOnClickListener {
            Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show()
        }

        // 安全中心
        binding.itemSecurity.setOnClickListener {
            Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show()
        }

        // 清除缓存
        binding.itemClearCache.setOnClickListener {
            Toast.makeText(activity, "缓存已清除", Toast.LENGTH_SHORT).show()
        }

        // 检查更新
        binding.itemCheckUpdate.setOnClickListener {
            Toast.makeText(activity, "当前已是最新版本", Toast.LENGTH_SHORT).show()
        }

        // 关于我们
        binding.itemAbout.setOnClickListener {
            Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show()
        }

        // GitHub
        binding.itemGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/xinpengfei520/iMoney"))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
