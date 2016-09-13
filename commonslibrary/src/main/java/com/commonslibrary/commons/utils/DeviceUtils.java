/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.commonslibrary.commons.utils;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;

import com.commonslibrary.commons.config.SystemConfig;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

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
     * 计算SD卡的剩余空间
     *
     * @param unit <ul>
     *             <li>{@link MemoryUnit#BYTE}: 字节</li>
     *             <li>{@link MemoryUnit#KB}  : 千字节</li>
     *             <li>{@link MemoryUnit#MB}  : 兆</li>
     *             <li>{@link MemoryUnit#GB}  : GB</li>
     *             </ul>
     * @return 返回-1，说明SD卡不可用，否则返回SD卡剩余空间
     */
    public static double getFreeSpace(FileUtils.MemoryUnit unit) {
        if (SystemConfig.isSDCardEnable()) {
            try {
                StatFs stat = new StatFs(SystemConfig.getSDCardPath());
                long blockSize, availableBlocks;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    availableBlocks = stat.getAvailableBlocksLong();
                    blockSize = stat.getBlockSizeLong();
                } else {
                    availableBlocks = stat.getAvailableBlocks();
                    blockSize = stat.getBlockSize();
                }
                return FileUtils.byte2Size(availableBlocks * blockSize, unit);
            } catch (Exception e) {
                e.printStackTrace();
                return -1.0;
            }
        } else {
            return -1.0;
        }
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
        if (!AppUtils.checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
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
        if (!AppUtils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return imei;
        }
        String deviceId;
        if (isPhone(context)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 获取手机状态信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @param context 上下文
     * @return DeviceId(IMEI) = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * honeType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    public static String getPhoneStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String str = "";
        str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + tm.getLine1Number() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "honeType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        return str;
    }

    /**
     * 判断设备是否是手机
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
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
        if (!AppUtils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
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
     * 获取本地IP
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        if (!AppUtils.checkPermission(context, Manifest.permission.INTERNET)) {
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
    public static String getNativePhoneNumber(Context context) {

        if (!AppUtils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
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
        if (!AppUtils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
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

        if (!AppUtils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
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
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
