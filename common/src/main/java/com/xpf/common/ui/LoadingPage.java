package com.xpf.common.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xpf.common.R;
import com.xpf.common.cons.ResultState;
import com.xpf.common.utils.UIUtils;

/**
 * Created by xpf on 2016/11/14 :)
 * Wechat:18091383534
 * Function:联网加载的公共页面
 */

public abstract class LoadingPage extends FrameLayout {

    // 1.提供联网操作的4中状态
    private final int STATE_LOADING = 1; // 加载状态
    private final int STATE_ERROR = 2;   // 联网失败的状态
    private final int STATE_EMPTY = 3;   // 联网成功,但是返回数据为空的状态
    private final int STATE_SUCCESS = 4; // 联网成功,且正确返回数据的状态

    private Context mContext;
    private ResultState mResultState; // 联网请求结果枚举类
    private int mCurrentState = STATE_LOADING; // 表示当前的状态

    // 2.提供4个不同的页面
    private View view_loading;
    private View view_error;
    private View view_empty;
    private View view_success;

    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化必要的View,并指明视图显示宽高的参数
     */
    private void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (view_loading == null) {
            view_loading = UIUtils.getView(R.layout.page_loading);
            addView(view_loading, params);
        }
        if (view_error == null) {
            view_error = UIUtils.getView(R.layout.page_error);
            addView(view_error, params);
        }
        if (view_empty == null) {
            view_empty = UIUtils.getView(R.layout.page_empty);
            addView(view_empty, params);
        }

        showSafePage();
    }

    /**
     * 根据state_current的值,决定显示哪个具体的View
     */
    private void showSafePage() {
        // 更新界面的操作需要在主线程中执行
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPage(); // 加载显示具体的view
            }
        });
    }

    /**
     * 主线程中:根据state_current的值,决定显示哪个具体的View
     */
    private void showPage() {
        view_loading.setVisibility(mCurrentState == STATE_LOADING ? View.VISIBLE : View.GONE);
        view_error.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE : View.GONE);
        view_empty.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE : View.GONE);

        if (view_success == null) {
            // view_success = UIUtils.getView(layoutId());
            // 修改为如下的加载:
            view_success = View.inflate(mContext, layoutId(), null);
            addView(view_success);
        }
        view_success.setVisibility(mCurrentState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
    }

    /**
     * 执行联网显示操作
     */
    public void show() {
        // 先判断url是否为空
        if (!TextUtils.isEmpty(url())) {
            mResultState = ResultState.SUCCESS;
            mResultState.setContent("");
            loadPage();
        } else {
            mResultState = ResultState.ERROR;
            mResultState.setContent("");
        }

        sendHttpRequest();
    }

    /**
     * 发送联网请求
     */
    private void sendHttpRequest() {
        String requestUrl = url();
        RequestParams params = params();
        // && (params != null)
        if ((!TextUtils.isEmpty(requestUrl))) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(requestUrl, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    if (TextUtils.isEmpty(content)) { //"" 或 null
                        mResultState = ResultState.EMPTY;
                        mResultState.setContent("");
                    } else {
                        mResultState = ResultState.SUCCESS;
                        mResultState.setContent(content);
                    }
                    loadPage();
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    mResultState = ResultState.ERROR;
                    mResultState.setContent("");
                    loadPage();
                }
            });
        } else {
            mResultState = ResultState.ERROR;
            mResultState.setContent("");
            loadPage();
        }
    }

    /**
     * 根据resultState值加载显示不同的页面
     */
    private void loadPage() {
        switch (mResultState) {
            case ERROR:
                mCurrentState = STATE_ERROR;
                break;
            case EMPTY:
                mCurrentState = STATE_EMPTY;
                break;
            case SUCCESS:
                mCurrentState = STATE_SUCCESS;
                break;
        }
        showSafePage();

        if (mCurrentState == STATE_SUCCESS) { // 如果当前是联网成功的状态
            onSuccess(mResultState, view_success);
        }
    }

    /**
     * 让子类去实现如下方法
     *
     * @param resultState
     * @param view_success
     */
    protected abstract void onSuccess(ResultState resultState, View view_success);

    protected abstract RequestParams params();

    protected abstract String url();

    public abstract int layoutId();

}
