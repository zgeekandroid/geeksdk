package com.commonslibrary.commons.config;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


import com.commonslibrary.commons.utils.FileUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * date        :  2015-12-02  10:27
 * author      :  Mickaecle gizthon
 * description :
 */
public class SystemConfig {
    private static String systemRootDir = "commonSDK";
    private final static String systemImageDir = "image";
    private final static String systemFileDir = "file";
    private final static String systemLogDir = "log";
    private final static String systemBaiduDir = "baidu";
    private static String sdcardDir = "";

    private final static String serverHost = "";

    private static boolean debug = false;

    public static void setSystemRootDir(String systemRootDir) {
        SystemConfig.systemRootDir = systemRootDir;
    }

    public static String getSystemRootDirName() {
        return SystemConfig.systemRootDir;
    }


    public static void setDebug(boolean debug) {
        SystemConfig.debug = debug;
    }

    /**
     * is or not debug model
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * the root path
     */
    public static String getSystemRootDir() {
        getSdcardDir();
        String fileDir = sdcardDir + File.separator + systemRootDir + File.separator;
        createDir(fileDir);
        return fileDir;
    }

    /**
     * sdcard directory
     */
    public static String getSdcardDir() {
        if (TextUtils.isEmpty(sdcardDir)) {
            sdcardDir = getExternalSdCardPath();
        }

        return sdcardDir;
    }

    /**
     * file directory
     */
    public static String getSystemFileDir() {
        String fileDir = getSystemRootDir() + systemFileDir + File.separator;
        createDir(fileDir);
        return fileDir;
    }

    /**
     * image directory
     */
    public static String getSystemImageDir() {
        String imageDir = getSystemRootDir() + systemImageDir + File.separator;
        createDir(imageDir);
        return imageDir;
    }

    /**
     * log directory
     */
    public static String getSystemLogDir() {
        String logDir = getSystemRootDir() + systemLogDir + File.separator;
        createDir(logDir);
        return logDir;
    }

    public static String getSystemBaiduDir() {
        String logDir = getSystemRootDir() + systemBaiduDir + File.separator;
        createDir(logDir);
        return logDir;
    }

    /**
     * server host or ip address
     */
    public static String getServerHost() {
        return "";
    }

    public static void createDir(String fileDir) {
        File localFile = new File(fileDir);

        if (!localFile.exists()) {
             if (localFile.mkdirs()){
                 //
             }
        }

    }


    /**
     * 获取扩展SD卡存储目录
     * <p>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    public static String getExternalSdCardPath() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
            long megAvailable = bytesAvailable / 1048576;
            if (megAvailable > 0.5) {
                File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                String path = "";
                if (sdCardFile.isDirectory() && sdCardFile.canWrite()) {
                    path = sdCardFile.getAbsolutePath();

                    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",Locale.CHINA).format(new Date());
                    File testWritable = new File(sdCardFile, "test_" + timeStamp);

                    if (testWritable.mkdirs()) {
                        testWritable.delete();
                    } else {
                        path = "/mnt/sdcard";
                    }
                }
                return path;
            }

        }

        String sdCardFile1 = getNewSdcard();
        if (sdCardFile1 != null) return sdCardFile1;

        return "/mnt/sdcard";
    }

    @Nullable
    private static String getNewSdcard() {
        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.CHINA).format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
     *
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        String[] toSearch = FileUtils.readFileByLines("/etc/vold.fstab").split(" ");
        ArrayList<String> out = new ArrayList<String>();
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i].contains("dev_mount")) {
                if (new File(toSearch[i + 2]).exists()) {
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

}
