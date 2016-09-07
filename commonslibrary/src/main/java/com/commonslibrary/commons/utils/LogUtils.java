package com.commonslibrary.commons.utils;

import android.text.TextUtils;
import android.util.Log;

import com.commonslibrary.commons.config.SystemConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.StringWriter;


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
        className = className.substring(0,className.length() - 5);
        buffer.append("[at ");
        buffer.append(className);
        buffer.append(".");
        buffer.append(methodName);
        buffer.append("(");
        buffer.append(className);
        buffer.append(".java:");
        buffer.append(lineNumber);
        buffer.append(") ]\n");
        buffer.append(prettyJson(log));

        return buffer.toString();
    }

    public static String prettyJson(String body) {
        if (TextUtils.isEmpty(body)) {
            return body;
        }
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("\u00A0\u00A0");
            JsonElement jsonElement = new JsonParser().parse(body);
            gson.toJson(jsonElement, jsonWriter);
            return stringWriter.toString();
        } catch (JsonParseException e) {
            return body;
        }
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
            Log.d(TAG, createLog(String.valueOf(message)));
        }

    }

    public static void i(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(TAG, createLog(String.valueOf(message)));
        }
    }

    public static void w(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(TAG, createLog(String.valueOf(message)));
        }
    }

    public static void e(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG, createLog(String.valueOf(message)));
        }
    }

}
