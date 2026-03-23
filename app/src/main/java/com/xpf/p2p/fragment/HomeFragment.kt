package com.xpf.p2p.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.alibaba.fastjson.JSON
import com.xpf.p2p.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.squareup.picasso.Picasso
import com.xpf.p2p.R
import com.xpf.p2p.constants.ApiRequestUrl
import com.xpf.p2p.entity.Image
import com.xpf.p2p.entity.Index
import com.xpf.p2p.entity.Product

/**
 * Created by xpf on 2016/11/11 :)
 * Function:
 */
class HomeFragment : Fragment() {

    private lateinit var tvHomeRate: TextView
    private var index: Index? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(activity, R.layout.fragment_home, null)
        tvHomeRate = view.findViewById(R.id.tv_home_rate)
        initData()
        return view
    }

    private fun initData() {
        RetrofitClient.apiService.post(ApiRequestUrl.INDEX).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val content = response.body()?.string() ?: return
                val jsonObject = JSON.parseObject(content)
                val proInfo = jsonObject.getString("proInfo")
                val product = JSON.parseObject(proInfo, Product::class.java)

                val imageArr = jsonObject.getString("imageArr")
                val images = JSON.parseArray(imageArr, Image::class.java)

                index = Index().apply {
                    this.product = product
                    this.images = images
                }

                val yearRate = index!!.product!!.yearRate
                tvHomeRate.text = "$yearRate%"
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(activity, "获取服务器数据失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class MyPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return index?.images?.size ?: 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(activity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val imaurl = index!!.images!![position].IMAURL
            Picasso.get().load(imaurl).into(imageView)
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}
