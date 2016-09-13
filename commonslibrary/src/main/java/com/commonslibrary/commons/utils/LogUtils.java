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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class LogUtils {
    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

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

    public static boolean isfomat = false;
    public static boolean isWrite = false;

    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// 日志文件格式
    private static final String MYLOGFILEName = "log.txt";// 本类输出的日志文件名称
    private static final int SDCARD_LOG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天数
    /**
     * 打开日志文件并写入日志
     *
     * @return *
     */
    public static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
        if (!isWrite){
            return;
        }
        Date nowtime = new Date();
        String needWriteFiel =  logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                + "    " + tag + "    " + text;
        File file = new File(SystemConfig.getSystemLogDir(), needWriteFiel
                + MYLOGFILEName);
        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     */
    public static void delFile() {// 删除日志文件
        String needDelFiel = logfile.format(getDateBefore());
        File file = new File(SystemConfig.getSystemLogDir(), needDelFiel + MYLOGFILEName);
        FileUtils.deleteFile(file);
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }

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
        if (isfomat){
            buffer.append(prettyJson(log));
        }else{
            buffer.append(log);
        }

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
            writeLogtoFile("v",TAG,String.valueOf(message));
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
            writeLogtoFile("d",TAG,String.valueOf(message));
        }

    }

    public static void i(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(TAG, createLog(String.valueOf(message)));
            writeLogtoFile("i",TAG,String.valueOf(message));
        }
    }

    public static void w(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(TAG, createLog(String.valueOf(message)));
            writeLogtoFile("w",TAG,String.valueOf(message));
        }
    }

    public static void e(Object message) {
        if (isPrint) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG, createLog(String.valueOf(message)));
            writeLogtoFile("e",TAG,String.valueOf(message));
        }
    }

}
