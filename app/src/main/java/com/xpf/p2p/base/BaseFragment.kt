package com.xpf.p2p.base

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.loopj.android.http.RequestParams
import com.xpf.p2p.constants.ResultState
import com.xpf.p2p.utils.UIUtils
import com.xpf.p2p.widget.LoadingPage

/**
 * Fragment 基类（Kotlin 版本）
 */
abstract class BaseFragment : Fragment() {

    var loadingPage: LoadingPage? = null
    protected var mContext: Context? = null
    protected var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = UIUtils.getView(getLayoutId())
        mContext = activity
        val url = getUrl()
        return if (TextUtils.isEmpty(url)) {
            initData(null)
            mView
        } else {
            loadingPage = object : LoadingPage(activity) {
                override fun layoutId(): Int = getLayoutId()

                override fun onSuccess(resultState: ResultState, view_success: View) {
                    initData(resultState.content)
                }

                override fun params(): RequestParams? = getParams()

                override fun url(): String = getUrl()
            }
            loadingPage
        }
    }

    abstract fun getLayoutId(): Int

    protected abstract fun getUrl(): String

    protected abstract fun getParams(): RequestParams?

    protected abstract fun initData(content: String?)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        super.onActivityCreated(savedInstanceState)
        val url = getUrl()
        if (!TextUtils.isEmpty(url)) {
            show()
        }
    }

    fun show() {
        loadingPage?.show()
    }
}
