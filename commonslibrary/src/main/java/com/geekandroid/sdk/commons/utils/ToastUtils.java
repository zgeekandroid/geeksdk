package com.geekandroid.sdk.commons.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class ToastUtils {
    public static void show(Context context,String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();
    }
}
