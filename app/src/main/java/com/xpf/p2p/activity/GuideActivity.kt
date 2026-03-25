package com.xpf.p2p.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.xpf.p2p.App
import com.xpf.p2p.R
import com.xpf.p2p.adapter.VpGuideAdapter
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.databinding.ActivityGuideBinding
import com.xpf.p2p.ui.login.view.LoginActivity
import com.xpf.p2p.utils.SpUtil
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/11 :)
 * Function:引导页
 */
class GuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuideBinding
    private var leftMarg: Int = 0
    private var widthDpi: Int = 0
    private lateinit var mImageViews: ArrayList<ImageView>
    private val mResIds = intArrayOf(R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvRightAway.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        initData()
        initImageViewList()
    }

    private fun initImageViewList() {
        mImageViews = ArrayList()
        for (resId in mResIds) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(resId)
            mImageViews.add(imageView)
        }
        if (mImageViews.isNotEmpty()) {
            binding.viewPager.adapter = VpGuideAdapter(mImageViews)
        }
    }

    private fun initData() {
        widthDpi = UIUtils.dp2px(8)
        for (i in mResIds.indices) {
            val point = ImageView(this)
            point.setImageResource(R.drawable.point_gray)
            val params = LinearLayout.LayoutParams(widthDpi, widthDpi)
            if (i != 0) {
                params.leftMargin = widthDpi
            }
            point.layoutParams = params
            binding.llPointGroup.addView(point)
        }

        binding.ivRedPoint.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.ivRedPoint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                leftMarg = binding.llPointGroup.getChildAt(1).left - binding.llPointGroup.getChildAt(0).left
            }
        })

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val leftMargin = (position + positionOffset) * leftMarg
                val params = binding.ivRedPoint.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = leftMargin.toInt()
                binding.ivRedPoint.layoutParams = params
            }

            override fun onPageSelected(position: Int) {
                binding.tvRightAway.visibility = if (position == 3) View.VISIBLE else View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onResume() {
        super.onResume()
        SpUtil.getInstance(App.context).save(SpKey.IS_NEED_GUIDE, false)
    }
}
