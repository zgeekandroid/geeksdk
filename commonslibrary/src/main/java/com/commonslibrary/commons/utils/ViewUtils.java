package com.commonslibrary.commons.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * date        :  2016-02-24  15:07
 * author      :  Mickaecle gizthon
 * description :
 */
public class ViewUtils {
    /**
     * 显示布局
     */
    public static void showView(View view) {
        if (view != null && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            setShowAnimation(view, 400);
        }
    }

    /**
     * 隐藏布局
     */
    public static void hideView(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 渐现动画
     *
     * @param view
     * @param duration
     */
    public static void setShowAnimation(View view, int duration) {
        AlphaAnimation mShowAnimation = null;
        if (null == view || duration < 0) {
            return;
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }
}
