package com.atguigu.p2p.ui.randomLayout;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomLayout extends ViewGroup {

    private Random mRdm;
    /**
     * X分布规则性，该值越高，子view在x方向的分布越规则、平均。最小值为1。
     */
    private int mXRegularity;
    /**
     * Y分布规则性，该值越高，子view在y方向的分布越规则、平均。最小值为1。
     */
    private int mYRegularity;
    /**
     * 区域个数
     */
    private int mAreaCount;
    /**
     * 区域的二维数组
     */
    private int[][] mAreaDensity;
    /**
     * 存放已经确定位置的View
     */
    private Set<View> mFixedViews;
    /**
     * 提供子View的adapter
     */
    private Adapter mAdapter;
    /**
     * 记录被回收的View，以便重复利用
     */
    private List<View> mRecycledViews;
    /**
     * 是否已经layout
     */
    private boolean mLayouted;
    /**
     * 计算重叠时候的间距
     */
    private int mOverlapAdd = 2;

    /**
     * 构造方法
     */
    public RandomLayout(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        mLayouted = false;
        mRdm = new Random();
        setRegularity(1, 1);
        mFixedViews = new HashSet<View>();
        mRecycledViews = new LinkedList<View>();
    }

    public boolean hasLayouted() {
        return mLayouted;
    }

    /**
     * 设置mXRegularity和mXRegularity，确定区域的个数
     */
    public void setRegularity(int xRegularity, int yRegularity) {
        if (xRegularity > 1) {
            this.mXRegularity = xRegularity;
        } else {
            this.mXRegularity = 1;
        }
        if (yRegularity > 1) {
            this.mYRegularity = yRegularity;
        } else {
            this.mYRegularity = 1;
        }
        this.mAreaCount = mXRegularity * mYRegularity;//个数等于x方向的个数*y方向的个数
        this.mAreaDensity = new int[mYRegularity][mXRegularity];//存放区域的二维数组
    }

    /**
     * 设置数据源
     */
    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
    }

    /**
     * 重新设置区域，把所有的区域记录都归0
     */
    private void resetAllAreas() {
        mFixedViews.clear();
        for (int i = 0; i < mYRegularity; i++) {
            for (int j = 0; j < mXRegularity; j++) {
                mAreaDensity[i][j] = 0;
            }
        }
    }

    /**
     * 把复用的View加入集合，新加入的放入集合第一个。
     */
    private void pushRecycler(View scrapView) {
        if (null != scrapView) {
            mRecycledViews.add(0, scrapView);
        }
    }

    /**
     * 取出复用的View，从集合的第一个位置取出
     */
    private View popRecycler() {
        final int size = mRecycledViews.size();
        if (size > 0) {
            return mRecycledViews.remove(0);
        } else {
            return null;
        }
    }

    /**
     * 产生子View，这个就是listView复用的简化版，但是原理一样
     */
    private void generateChildren() {
        if (null == mAdapter) {
            return;
        }
        // 先把子View全部存入集合
        final int childCount = super.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            pushRecycler(super.getChildAt(i));
        }
        // 删除所有子View
        super.removeAllViewsInLayout();
        // 得到Adapter中的数据量
        final int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            //从集合中取出之前存入的子View
            View convertView = popRecycler();
            //把该子View作为adapter的getView的历史View传入，得到返回的View
            View newChild = mAdapter.getView(i, convertView);
            if (newChild != convertView) {//如果发生了复用，那么newChild应该等于convertView
                // 这说明没发生复用，所以重新把这个没用到的子View存入集合中
                pushRecycler(convertView);
            }
            //调用父类的方法把子View添加进来
            super.addView(newChild, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 重新分配区域
     */
    public void redistribute() {
        resetAllAreas();//重新设置区域
        requestLayout();
    }

    /**
     * 重新更新子View
     */
    public void refresh() {
        resetAllAreas();//重新分配区域
        generateChildren();//重新产生子View
        requestLayout();
    }

    /**
     * 重写父类的removeAllViews
     */
    @Override
    public void removeAllViews() {
        super.removeAllViews();//先删除所有View
        resetAllAreas();//重新设置所有区域
    }

    /**
     * 确定子View的位置，这个就是区域分布的关键
     */
    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        // 确定自身的宽高
        int thisW = r - l - this.getPaddingLeft() - this.getPaddingRight();
        int thisH = b - t - this.getPaddingTop() - this.getPaddingBottom();
        // 自身内容区域的右边和下边
        int contentRight = r - getPaddingRight();
        int contentBottom = b - getPaddingBottom();
        // 按照顺序存放把区域存放到集合中
        List<Integer> availAreas = new ArrayList<Integer>(mAreaCount);
        for (int i = 0; i < mAreaCount; i++) {
            availAreas.add(i);
        }

        int areaCapacity = (count + 1) / mAreaCount + 1;  //区域密度，表示一个区域内可以放几个View，+1表示至少要放一个
        int availAreaCount = mAreaCount; //可用的区域个数

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) { // gone掉的view是不参与布局
                continue;
            }

            if (!mFixedViews.contains(child)) {//mFixedViews用于存放已经确定好位置的View，存到了就没必要再次存放
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                // 先测量子View的大小
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.AT_MOST);//为子View准备测量的参数
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), MeasureSpec.AT_MOST);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                // 子View测量之后的宽和高
                int childW = child.getMeasuredWidth();
                int childH = child.getMeasuredHeight();
                // 用自身的高度去除以分配值，可以算出每一个区域的宽和高
                float colW = thisW / (float) mXRegularity;
                float rowH = thisH / (float) mYRegularity;

                while (availAreaCount > 0) { //如果使用区域大于0，就可以为子View尝试分配
                    int arrayIdx = mRdm.nextInt(availAreaCount);//随机一个list中的位置
                    int areaIdx = availAreas.get(arrayIdx);//再根据list中的位置获取一个区域编号
                    int col = areaIdx % mXRegularity;//计算出在二维数组中的位置
                    int row = areaIdx / mXRegularity;
                    if (mAreaDensity[row][col] < areaCapacity) {// 区域密度未超过限定，将view置入该区域
                        int xOffset = (int) colW - childW; //区域宽度 和 子View的宽度差值，差值可以用来做区域内的位置随机
                        if (xOffset <= 0) {
                            xOffset = 1;
                        }
                        int yOffset = (int) rowH - childH;
                        if (yOffset <= 0) {
                            yOffset = 1;
                        }
                        // 确定左边，等于区域宽度*左边的区域
                        params.mLeft = getPaddingLeft() + (int) (colW * col + mRdm.nextInt(xOffset));
                        int rightEdge = contentRight - childW;
                        if (params.mLeft > rightEdge) {//加上子View的宽度后不能超出右边界
                            params.mLeft = rightEdge;
                        }
                        params.mRight = params.mLeft + childW;

                        params.mTop = getPaddingTop() + (int) (rowH * row + mRdm.nextInt(yOffset));
                        int bottomEdge = contentBottom - childH;
                        if (params.mTop > bottomEdge) {//加上子View的宽度后不能超出右边界
                            params.mTop = bottomEdge;
                        }
                        params.mBottom = params.mTop + childH;

                        if (!isOverlap(params)) {//判断是否和别的View重叠了
                            mAreaDensity[row][col]++;//没有重叠，把该区域的密度加1
                            child.layout(params.mLeft, params.mTop, params.mRight, params.mBottom);//布局子View
                            mFixedViews.add(child);//添加到已经布局的集合中
                            break;
                        } else {//如果重叠了，把该区域移除，
                            availAreas.remove(arrayIdx);
                            availAreaCount--;
                        }
                    } else {// 区域密度超过限定，将该区域从可选区域中移除
                        availAreas.remove(arrayIdx);
                        availAreaCount--;
                    }
                }
            }
        }
        mLayouted = true;
    }

    /**
     * 计算两个View是否重叠，如果重叠，那么他们之间一定有一个矩形区域是共有的
     */
    private boolean isOverlap(LayoutParams params) {
        int l = params.mLeft - mOverlapAdd;
        int t = params.mTop - mOverlapAdd;
        int r = params.mRight + mOverlapAdd;
        int b = params.mBottom + mOverlapAdd;

        Rect rect = new Rect();

        for (View v : mFixedViews) {
            int vl = v.getLeft() - mOverlapAdd;
            int vt = v.getTop() - mOverlapAdd;
            int vr = v.getRight() + mOverlapAdd;
            int vb = v.getBottom() + mOverlapAdd;
            rect.left = Math.max(l, vl);
            rect.top = Math.max(t, vt);
            rect.right = Math.min(r, vr);
            rect.bottom = Math.min(b, vb);
            if (rect.right >= rect.left && rect.bottom >= rect.top) {
                return true;
            }
        }
        return false;
    }

    /**
     * 内部类、接口
     */
    public static interface Adapter {

        public abstract int getCount();

        public abstract View getView(int position, View convertView);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        private int mLeft;
        private int mRight;
        private int mTop;
        private int mBottom;

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}
