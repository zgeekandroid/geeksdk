package com.geekandroid.sdk.sample.utils;

import android.util.Log;

import com.geekandroid.common.config.SystemConfig;


/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class LogUtils {
    public static final String TAG = "SDK";


    private static boolean DEBUG = SystemConfig.isDebug();

    /**
     * 部分手机显示不出来,如华为
     *
     * @param string
     */
    @Deprecated
    private static void v(Object string) {
        if (DEBUG) {
            Log.v(TAG, String.valueOf(string));
        }

    }

    /**
     * 部分手机显示不出来
     *
     * @param string
     */
    @Deprecated
    private static void d(Object string) {
        if (DEBUG) {
            Log.d(TAG, String.valueOf(string));
        }

    }

    public static void i(Object string) {
        if (DEBUG) {
            Log.i(TAG, String.valueOf(string));
        }
    }

    public static void w(Object string) {
        if (DEBUG) {
            Log.w(TAG, String.valueOf(string));
        }
    }

    public static void e(Object string) {
        if (DEBUG) {
            Log.e(TAG, String.valueOf(string));
        }
    }

}
