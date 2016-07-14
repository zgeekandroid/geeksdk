package com.commonslibrary.commons.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * date        :  2016-01-27  13:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class DeviceUtils {

    /**
     * 电量值
     * Battery level;
     */
    private static int sBatteryLevel = 0;

    private DeviceUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }



    public static int getDefaultImageWidth(Context mContext) {
        return getScreenWidth(mContext);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }


    /**
     * 检查是否具有获取某些设备信息的权限
     * Check ther permission required;
     *
     * @param context
     * @param permName 权限名; Permission name;
     * @return
     */
    public static boolean checkPermission(Context context, String permName) {
        return context.getPackageManager().checkPermission(permName, context.getPackageName())
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获取MAC地址
     * Get MAC address;
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress = null;
        if (!checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            return macAddress;
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (null == wifiManager) {
            return macAddress;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (null == wifiInfo) {
            return macAddress;
        }

        macAddress = wifiInfo.getMacAddress();
        return null == macAddress ? null : macAddress.toUpperCase();
    }

    /**
     * 获取IMEI信息
     * Get IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context) {
        String imei = null;
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return imei;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            return imei;
        }

        imei = telephonyManager.getDeviceId();
        return imei.toUpperCase();
    }

    /**
     * 获取IMSI
     * Get IMSI
     *
     * @param context
     * @return
     */
    public static String getDeviceIMSI(Context context) {
        String imsi = null;
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return imsi;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            return imsi;
        }

        imsi = telephonyManager.getSubscriberId();
        return null == imsi ? null : imsi.toUpperCase();
    }

    /**
     * 获取Device model
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取生产商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取屏幕高度(pixels)
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度(pixels)
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取APP的版本名
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("Cannot get app version name" + e);
        }
        return versionName;
    }

    /**
     * 获取APP的版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("Cannot get app version name" + e);
        }
        return versionCode;
    }

    /**
     * 获取联网状态
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {

        if (!isNetworkConnected(context)) {
            return "NONE";
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return null;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
            return "WIFI";
        }

        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return null;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (null == telephonyManager) {
            return null;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "MOBILE-2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "MOBILE-3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "MOBILE-4G";
            default:
                return "MOBILE-UNKNOWN";
        }
    }

    /**
     * 获取本地IP
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        if (!checkPermission(context, Manifest.permission.INTERNET)) {
            return null;
        }

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            if (en == null) {
                return "";
            }

            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("Cannot get local ipaddress" + e);
        }

        return null;
    }


    /**
     * 获取包名
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取平台类型: Android
     *
     * @return
     */
    public static String getPlatform() {
        return "Android";
    }

    /**
     * 检查网络是否连接
     *
     * @param context
     * @return
     */
    private static boolean isNetworkConnected(Context context) {
        boolean isNetworkConnected = false;

        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (null != networkInfo && networkInfo.isConnected());
    }

    /**
     * 获取Android的版本号
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId == null ? null : androidId.toUpperCase();
    }

    /**
     * 获取电话号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNo(Context context) {

        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }

        String phoneNo = tm.getLine1Number();
        return phoneNo == null ? "" : phoneNo;
    }

    /**
     * 判断是否为模拟器(普通方式)
     * It is hard to recognize all the emulator in the market. This is a normal way.
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return false;
        }

        if (Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk")) {
            return true;
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        String imei = tm.getDeviceId();
        if ((imei != null && imei.equals("000000000000000"))) {
            return true;
        }

        return false;
    }

    /**
     * 获取基站定位数据
     *
     * @param context
     * @return
     */
    public static String getCellLocation(Context context) {

        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }

        CellLocation cellLocation = tm.getCellLocation();
        if (cellLocation == null) {
            return "";
        }
        return cellLocation.toString();
    }

    /**
     * 注册电池监听广播
     * register to obtain the battery info;
     */
    public static void registerBatteryReceiver(Context context) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sBatteryLevel = (int) (100f * intent
                        .getIntExtra(BatteryManager.EXTRA_LEVEL, 0) / intent.getIntExtra
                        (BatteryManager.EXTRA_SCALE, 100));
            }
        };
        context.registerReceiver(batteryReceiver, filter);
    }

    /**
     * 获取app上传流量
     * Get the tx traffic of this APP;
     *
     * @return
     */
    public static long txBytesInfoGenerate() {
        return TrafficStats.getUidTxBytes(android.os.Process.myUid());
    }

    /**
     * 获取app接收流量
     * Get the rx traffic of this APP;
     *
     * @return
     */
    public static long rxBytesInfoGenerate() {
        return TrafficStats.getUidRxBytes(android.os.Process.myUid());
    }


    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isRunningInFront(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
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
     * 安装
     *
     * @param context 接收外部传进来的context
     */
    public static void install(Context context,String url) {
        // 核心是下面几句代码
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(url)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = String.valueOf(metaData.get(metaKey));
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return metaValue;
    }
}
