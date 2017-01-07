package com.xpf.p2p.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.xpf.p2p.utils.UIUtils;

/**
 * Created by xpf on 2016/11/14 :)
 * Wechat:18091383534
 * Function:自定义带有回弹效果的ScrollView
 */

public class MyScrollView extends ScrollView {

    private View childView;   // ScrollView中的子视图
    private int lastX, lastY; // 记录上一次x,y轴坐标的位置
    private int downX, downY; // 记录down事件时,x,y轴的坐标位置
    private Rect normal = new Rect();         // 用于记录临界状态时的left,top,right,bottom的坐标
    private boolean isFinishAnimation = true; // 判断动画是否结束


    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 获取内部的子视图
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            childView = getChildAt(0);
        }
    }

    /**
     * 如果返回值为true表示拦截子视图的处理,如果返回false表示不拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean isIntercept = false; // 默认不拦截
        int eventX = (int) ev.getX();
        int eventY = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastX = eventX;
                downY = lastY = eventY;
                break;

            case MotionEvent.ACTION_MOVE:
                // 记录水平方向和垂直方向移动的距离
                int totalX = Math.abs(eventX - downX);
                int totalY = Math.abs(eventY - downY);

                // 如果竖直方向移动的距离大于水平方向移动的距离,且竖直方向移动的距离超过10就拦截
                if (totalX < totalY && totalY > UIUtils.dp2px(10)) {
                    isIntercept = true;
                }
                lastX = eventX;
                lastY = eventY;
                break;
        }

        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (childView == null || !isFinishAnimation) {
            return super.onTouchEvent(ev);
        }

        int eventY = (int) ev.getY(); // 获取相对于当前视图的y轴坐标

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastY = eventY;
                break;

            case MotionEvent.ACTION_MOVE:
                int dy = eventY - lastY;    // 获取竖直方向的移动量
                if (isNeedMove()) {
                    if (normal.isEmpty()) { // 如果没有记录过left,top,right,bottom的值,就返回true
                        // 记录临界状态的left,top,right,bottom坐标
                        normal.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
                    }
                    // 给视图重新布局
                    childView.layout(childView.getLeft(), childView.getTop() + dy / 2, childView.getRight(), childView.getBottom() + dy / 2);
                }
                lastY = eventY; // 重新赋值
                break;

            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    TranslateAnimation animation = new TranslateAnimation(0, 0, 0, normal.bottom - childView.getBottom());
                    animation.setDuration(200);
                    childView.startAnimation(animation);

                    // 设置动画的监听
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            childView.clearAnimation(); // 清除动画
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom); // 重新布局
                            normal.setEmpty(); // 清空normal中left、top、right、bottom数据
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    // 是否需要使用动画
    private boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 判断是否需要按照我们自定义的方式重新布局
    private boolean isNeedMove() {

        int measureHeight = childView.getMeasuredHeight(); // 获取子视图测量的高度
        int height = this.getHeight();   // 得到屏幕的高度
        Log.e("TAG", "measureHeight" + measureHeight + ",height = " + height);

        int dy = measureHeight - height; // 获取二者的距离差
        int scrollY = this.getScrollY(); // 获取当前视图在y轴方向上移动的位移 (最初:0,上移:+,下移:-)

        if (scrollY <= 0 || scrollY >= dy) {
            return true;
        }
        return false;
    }
}
