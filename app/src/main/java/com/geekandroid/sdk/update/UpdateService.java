package com.geekandroid.sdk.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.commonslibrary.commons.config.SystemConfig;
import com.commonslibrary.commons.net.RequestCallBack;
import com.geekandroid.sdk.sample.utils.DialogUtils;
import com.geekandroid.sdk.sample.utils.FileUtils;
import com.geekandroid.sdk.update.domain.DownloadInfo;
import com.geekandroid.sdk.update.domain.VersionBean;
import com.geekandroid.sdk.update.net.NetUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/25.
 */
public class UpdateService extends Service {
    private static Activity mActivity;
    private int serverVersionCode;
    private int localVersionCode;
    public static String serverURL = "";
    public static String fileName = "";
    public String filepath;
    public static String mfileDir;
    private ProgressDialog pd;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkVersions();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startService(Activity activity, String url, String name, String fileDir) {
        Intent intent = new Intent(activity, UpdateService.class);
        activity.startService(intent);
        mActivity = activity;
        serverURL = url;
        fileName = name;
        mfileDir = fileDir;
    }

    private void checkVersions() {
        localVersionCode = VersionChecker.getLocalVersionCode(this);
        VersionChecker.getServerVersionCode(this, localVersionCode, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                VersionBean versionBean = gson.fromJson(result, VersionBean.class);
                if (null != versionBean) {
                    serverVersionCode = (null != getAppVersionNew(versionBean)) ? Integer.parseInt(getAppVersionNew(versionBean)) : -1;
                    if (serverVersionCode > localVersionCode && !getFileIsExist(versionBean)) {
                        String dialog_certain = "马上下载";
                        String dialog_cancel = "下次提醒";
                        String title = "有新版本：";
                        if (null != versionBean) {
                            String description = "快点更新把";
                            DialogUtils.showUpdateDialog(mActivity, title, dialog_certain, dialog_cancel, description, new onDialogBtnListerner() {
                                @Override
                                public void sureClick() {
                                    doDownLoad();
                                }

                                @Override
                                public void cancelClick() {

                                }
                            });
                        }
                    }
                }
                boolean is = getFileIsExist(versionBean);
                if (serverVersionCode > localVersionCode && getFileIsExist(versionBean)) {
                    DialogUtils.showInstallDialog(mActivity, filepath);
                }
            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void doDownLoad() {
        pd = DialogUtils.showDownLoadDialog(mActivity);
        DownloadInfo downloadInfo = new DownloadInfo(serverURL, fileName + serverVersionCode + ".apk", SystemConfig.getSystemFileDir(), true);
        if (null != downloadInfo && downloadInfo.isDownLoad()) {
            String downLoadUrl = downloadInfo.getDownLoadUrl();
            final String fileName = downloadInfo.getFileName();
            final String fileDir = downloadInfo.getFileDir();
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", fileName);
            NetUtils.doDownLoad(downLoadUrl, map, new RequestCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i("lyy", "result:" + result);
                }

                @Override
                public void onFailure(String errorMessage, Exception exception) {
                }

                @Override
                public void onProgress(long byteWrite, long contentLength, boolean isDone) {
                    int progress = (int) byteWrite;
                    String filepath = fileDir + fileName;
                    File file = new File(filepath);
                    if (progress > 0 && contentLength > 0) {
                        if (progress > 0) {
                            pd.setMax((int) contentLength);
                            pd.setProgress(progress);
                        }
                    }
                    if (isDone && file.exists()) {
                        //可添加自己保留下载信息 方便实现开启APP文件询问安装
                        //下载完成 ，关掉服务,询问是否现在安装
                        if (null != filepath) {
                            pd.dismiss();
                            DialogUtils.showInstallDialog(mActivity, filepath);
                        }
                        stopSelf();
                    }
                }
            });
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static String getAppVersionNew(VersionBean versionBean) {
        return versionBean.getBackinfo().getApp_version_new();
    }

    private boolean getFileIsExist(VersionBean versionBean) {
        String fileName = "maicaime" + versionBean.getBackinfo().getApp_version_new() + ".apk";
        filepath = mfileDir + fileName;
        return FileUtils.isFileExists(filepath);
    }
}
