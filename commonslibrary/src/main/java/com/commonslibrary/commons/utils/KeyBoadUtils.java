package com.commonslibrary.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * date        :  2016-02-24  15:05
 * author      :  Mickaecle gizthon
 * description :
 */
public class KeyBoadUtils {
    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context mContext) {
        Activity activity = (Activity) mContext;
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }

    public static void hideSoftKeyboard(Context mContext, View view) {
        if ((mContext == null) || (view == null)){
            return;
        }
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null){
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


}
