package com.xpf.p2p.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.xpf.p2p.App
import com.xpf.p2p.R
import com.xpf.p2p.adapter.VpGuideAdapter
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.ui.login.view.LoginActivity
import com.xpf.p2p.utils.SpUtil
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/11 :)
 * Function:引导页
 */
class GuideActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var llPointGroup: LinearLayout
    private lateinit var ivRedPoint: ImageView
    private lateinit var rlPoints: RelativeLayout
    private lateinit var tvRightAway: TextView
    private var leftMarg: Int = 0
    private var widthDpi: Int = 0
    private lateinit var mImageViews: ArrayList<ImageView>
    private val mResIds = intArrayOf(R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        viewPager = findViewById(R.id.viewPager)
        llPointGroup = findViewById(R.id.llPointGroup)
        ivRedPoint = findViewById(R.id.ivRedPoint)
        rlPoints = findViewById(R.id.rlPoints)
        tvRightAway = findViewById(R.id.tvRightAway)
        tvRightAway.setOnClickListener {
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
            viewPager.adapter = VpGuideAdapter(mImageViews)
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
            llPointGroup.addView(point)
        }

        ivRedPoint.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                ivRedPoint.viewTreeObserver.addOnGlobalLayoutListener(this)
                leftMarg = llPointGroup.getChildAt(1).left - llPointGroup.getChildAt(0).left
            }
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val leftMargin = (position + positionOffset) * leftMarg
                val params = ivRedPoint.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = leftMargin.toInt()
                ivRedPoint.layoutParams = params
            }

            override fun onPageSelected(position: Int) {
                tvRightAway.visibility = if (position == 3) View.VISIBLE else View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onResume() {
        super.onResume()
        SpUtil.getInstance(App.context).save(SpKey.IS_NEED_GUIDE, false)
    }
}
