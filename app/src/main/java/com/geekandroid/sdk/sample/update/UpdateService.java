package com.geekandroid.sdk.sample.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.geekandroid.common.config.SystemConfig;
import com.geekandroid.sdk.sample.MainActivity;
import com.geekandroid.sdk.sample.domain.VersionEvent;
import com.geekandroid.sdk.sample.update.domain.DownloadInfo;
import com.geekandroid.sdk.sample.update.domain.VersionBean;
import com.geekandroid.sdk.sample.update.net.NetUtils;
import com.geekandroid.sdk.sample.update.net.RequestCallBack;
import com.geekandroid.sdk.sample.utils.DialogUtils;
import com.geekandroid.sdk.sample.utils.FileUtils;
import com.geekandroid.sdk.sample.utils.JsonUtils;
import com.geekandroid.sdk.sample.utils.SharePreferenceUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by lenovo on 2016/4/25.
 */
public class UpdateService extends Service {
    public String serverURL = "";
    private int serverVersionCode;
    private int localVersionCode;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
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
                        VersionEvent versionEvent = new VersionEvent();
                        versionEvent.setVersionBean(versionBean);
                        EventBus.getDefault().post(versionEvent);
                    }
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
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe
    public void onEventMainThread(VersionEvent event) {
        DownloadInfo downloadInfo = event.getDownloadInfo();
        if (null != downloadInfo && downloadInfo.isDownLoad()) {
            String downLoadUrl = downloadInfo.getDownLoadUrl();
            final String fileName = downloadInfo.getFileName();
            final String fileDir = downloadInfo.getFileDir();
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", fileName);
            NetUtils.doDownLoad(downLoadUrl, map, new RequestCallBack<String>() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onFailure(String errorMessage, Exception exception) {

                }

                @Override
                public void onProgress(long byteWrite, long contentLength, boolean isDone) {
                    int progress = (int) byteWrite;
                    String filepath = fileDir + fileName;
                    File file = new File(filepath);
                    VersionEvent progressVersionEvent = new VersionEvent();
                    if (progress > 0 && contentLength > 0) {
                        progressVersionEvent.setProgress(progress);
                        progressVersionEvent.setContentLength(contentLength);
                        EventBus.getDefault().post(progressVersionEvent);
                    }
                    if (isDone && file.exists()) {
                        //可添加自己保留下载信息 方便实现开启APP文件询问安装
                        //下载完成 ，关掉服务,询问是否现在安装
                        VersionEvent versionEvent = new VersionEvent();
                        versionEvent.setIsFinish(true);
                        versionEvent.setFileUrl(filepath);
                        EventBus.getDefault().post(versionEvent);
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
        String fileDir = SystemConfig.getSystemFileDir();
        String filepath = fileDir + fileName;
        return FileUtils.isFileExists(filepath);
    }
}
