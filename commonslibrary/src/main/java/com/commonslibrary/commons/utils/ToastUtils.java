package com.commonslibrary.commons.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class ToastUtils {
    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(Context context, String message) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }
        show(context, message, Gravity.CENTER);
    }

    public static void showDefault(Context context, String message) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, String message, int gravity) {

        if (null == context || TextUtils.isEmpty(message)) {
            return;
        }

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 10);
        toast.show();
    }
}
