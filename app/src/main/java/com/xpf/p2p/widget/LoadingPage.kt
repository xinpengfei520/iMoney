package com.xpf.p2p.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.xpf.p2p.R
import com.xpf.p2p.constants.ResultState
import com.xpf.p2p.utils.UIUtils

/**
 * Created by xpf on 2016/11/14 :)
 * Wechat:vancexin
 * Function:联网加载的公共页面
 */
abstract class LoadingPage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val STATE_LOADING = 1
    private val STATE_ERROR = 2
    private val STATE_EMPTY = 3
    private val STATE_SUCCESS = 4

    private val mContext: Context = context
    private var mResultState: ResultState? = null
    private var mCurrentState = STATE_LOADING

    private var view_loading: View? = null
    private var view_error: View? = null
    private var view_empty: View? = null
    private var view_success: View? = null

    init {
        init()
    }

    private fun init() {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (view_loading == null) {
            view_loading = UIUtils.getView(R.layout.page_loading)
            addView(view_loading, params)
        }
        if (view_error == null) {
            view_error = UIUtils.getView(R.layout.page_error)
            addView(view_error, params)
        }
        if (view_empty == null) {
            view_empty = UIUtils.getView(R.layout.page_empty)
            addView(view_empty, params)
        }
        showSafePage()
    }

    private fun showSafePage() {
        UIUtils.runOnUiThread { showPage() }
    }

    private fun showPage() {
        view_loading!!.visibility = if (mCurrentState == STATE_LOADING) View.VISIBLE else View.GONE
        view_error!!.visibility = if (mCurrentState == STATE_ERROR) View.VISIBLE else View.GONE
        view_empty!!.visibility = if (mCurrentState == STATE_EMPTY) View.VISIBLE else View.GONE

        if (view_success == null) {
            view_success = View.inflate(mContext, layoutId(), null)
            addView(view_success)
        }
        view_success!!.visibility = if (mCurrentState == STATE_SUCCESS) View.VISIBLE else View.GONE
    }

    fun show() {
        if (!TextUtils.isEmpty(url())) {
            mResultState = ResultState.SUCCESS
            mResultState!!.content = ""
            loadPage()
        } else {
            mResultState = ResultState.ERROR
            mResultState!!.content = ""
        }
        sendHttpRequest()
    }

    private fun sendHttpRequest() {
        val requestUrl = url()
        val params = params()
        if (!TextUtils.isEmpty(requestUrl)) {
            val client = AsyncHttpClient()
            client.get(requestUrl, params, object : AsyncHttpResponseHandler() {
                override fun onSuccess(content: String?) {
                    if (TextUtils.isEmpty(content)) {
                        mResultState = ResultState.EMPTY
                        mResultState!!.content = ""
                    } else {
                        mResultState = ResultState.SUCCESS
                        mResultState!!.content = content
                    }
                    loadPage()
                }

                override fun onFailure(error: Throwable?, content: String?) {
                    mResultState = ResultState.ERROR
                    mResultState!!.content = ""
                    loadPage()
                }
            })
        } else {
            mResultState = ResultState.ERROR
            mResultState!!.content = ""
            loadPage()
        }
    }

    private fun loadPage() {
        when (mResultState) {
            ResultState.ERROR -> mCurrentState = STATE_ERROR
            ResultState.EMPTY -> mCurrentState = STATE_EMPTY
            ResultState.SUCCESS -> mCurrentState = STATE_SUCCESS
            else -> {}
        }
        showSafePage()
        if (mCurrentState == STATE_SUCCESS) {
            onSuccess(mResultState!!, view_success!!)
        }
    }

    protected abstract fun onSuccess(resultState: ResultState, view_success: View)

    protected abstract fun params(): RequestParams?

    protected abstract fun url(): String?

    abstract fun layoutId(): Int
}
