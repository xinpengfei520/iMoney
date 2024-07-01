package com.xpf.p2p.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;


/**
 * DrawUtils绘制矩形的工具类
 */
public class DrawUtils {

    // 提供一个指定颜色和圆角半径的Drawable对象
    public static GradientDrawable getDrawable(int rgb, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(rgb);  //填充颜色
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT); // shape矩形
        gradientDrawable.setCornerRadius(radius);  //四周圆角半径
        gradientDrawable.setStroke(UIUtils.dp2px(1), rgb); //边框厚度与颜色
        return gradientDrawable;
    }

    public static StateListDrawable getSelector(Drawable normalDrawable, Drawable pressDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        // 给当前的颜色选择器添加选中图片指向状态，未选中图片指向状态
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        // 设置默认状态
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }
}
