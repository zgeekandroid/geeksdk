package com.commonslibrary.commons.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 公共方法
 *
 * @author Samo
 */
public class CommonUtils {


    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context mContext) {
        Activity activity = (Activity) mContext;
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isRunningInFront(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            String packageName = topActivity.getPackageName().toString().trim();
            String packageName2 = context.getPackageName().toString().trim();
            if (packageName.equals(packageName2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断APP是否在后台运行
     *
     * @param appName app名字
     * @param context
     * @return 是否在后台运行的标志
     */
    public boolean isApplicationBroughtToBackground(String appName, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
        if (null == appProcesses) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(appName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
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

    /**
     * 保留一位小数
     *
     * @param price
     * @return
     */
    public static double convertDouble(double price) {
        BigDecimal b = new BigDecimal(price);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 保留一位小数
     */
    public static String convertOneDecimal(double price) {
        BigDecimal decimal = new BigDecimal(price);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 保留二位小数
     */
    public static String convertTwoDecimal(double price) {
        BigDecimal decimal = new BigDecimal(price);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static double convertDoubleTwoDecimal(double price) {
        BigDecimal decimal = new BigDecimal(price);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

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
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            verCode = context.getPackageManager().getPackageInfo(
                    "com.example.try_downloadfile_progress", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.example.try_downloadfile_progress", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }
}
