package com.xpf.p2p.fragment

import android.content.Context
import android.os.SystemClock
import android.text.TextUtils
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.squareup.picasso.Picasso
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.constants.ApiRequestUrl
import com.xpf.p2p.databinding.FragmentHomeBinding
import com.xpf.p2p.entity.Image
import com.xpf.p2p.entity.Index
import com.xpf.p2p.entity.Product
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader

/**
 * Created by xpf on 2016/11/11 :)
 * Function:使用Banner实现图片的轮番显示的效果,替换ViewPager + CirclePagerIndicator
 */
class HomeFragment2 : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var index: Index? = null
    private var currentProgress: Int = 0

    private val runnable = Runnable {
        binding.roundprogress.max = 100
        binding.roundprogress.progress = 0
        for (i in 0 until currentProgress) {
            binding.roundprogress.progress = binding.roundprogress.progress + 1
            SystemClock.sleep(20)
            binding.roundprogress.postInvalidate()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getUrl(): String = ApiRequestUrl.INDEX

    override fun getParams(): Map<String, String> = emptyMap()

    override fun initData(content: String?) {
        _binding = FragmentHomeBinding.bind(mView!!)

        if (!TextUtils.isEmpty(content)) {
            val jsonObject = JSON.parseObject(content)
            val proInfo = jsonObject.getString("proInfo")
            val product = JSON.parseObject(proInfo, Product::class.java)

            val imageArr = jsonObject.getString("imageArr")
            val images = JSON.parseArray(imageArr, Image::class.java)

            index = Index().apply {
                this.product = product
                this.images = images
            }

            // 设置Banner
            binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
            binding.banner.setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                    Picasso.get().load(path as String).into(imageView)
                }
            })

            val imageUrl = images.map { it.IMAURL }
            binding.banner.setImages(imageUrl)
            binding.banner.setBannerAnimation(Transformer.ZoomOutSlide)
            val titles = listOf("深情不及久伴，加息2%", "乐享活计划", "破茧重生", "安心钱包计划")
            binding.banner.setBannerTitles(titles)
            binding.banner.isAutoPlay(true)
            binding.banner.setDelayTime(3000)
            binding.banner.setIndicatorGravity(BannerConfig.RIGHT)
            binding.banner.start()

            val yearRate = index!!.product!!.yearRate
            binding.tvHomeRate.text = "$yearRate%"

            currentProgress = index!!.product!!.progress!!.toInt()
            Thread(runnable).start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
