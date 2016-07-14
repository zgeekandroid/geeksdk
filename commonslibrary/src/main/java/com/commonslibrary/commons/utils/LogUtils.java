package com.commonslibrary.commons.utils;

import android.util.Log;

import com.commonslibrary.commons.config.SystemConfig;


/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class LogUtils {

    private static boolean DEBUG = SystemConfig.isDebug();

    /**
     * 部分手机显示不出来,如华为
     *
     * @param string
     */

    public static final String TAG = "SDK";

    private static boolean isPrint = SystemConfig.isDebug();
    private static String className;
    private static String methodName;
    private static int lineNumber;

    private static String createLog(String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(className);
        buffer.append(" ");
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    /**
     * 部分手机显示不出来,如华为
     *
     * @param message
     */
    @Deprecated
    private static void v(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(TAG, createLog(String.valueOf(message)));
        }

    }

    /**
     * 部分手机显示不出来
     *
     * @param message
     */
    @Deprecated
    private static void d(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(TAG, createLog(createLog(String.valueOf(message))));
        }

    }

    public static void i(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(TAG, createLog(createLog(String.valueOf(message))));
        }
    }

    public static void w(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(TAG, createLog(createLog(String.valueOf(message))));
        }
    }

    public static void e(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG, createLog(createLog(String.valueOf(message))));
        }
    }

}
