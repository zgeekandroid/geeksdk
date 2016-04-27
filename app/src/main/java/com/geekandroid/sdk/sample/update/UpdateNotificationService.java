package com.geekandroid.sdk.sample.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.geekandroid.common.config.SystemConfig;
import com.geekandroid.sdk.sample.domain.VersionEvent;
import com.geekandroid.sdk.sample.update.domain.VersionBean;
import com.geekandroid.sdk.sample.update.net.RequestCallBack;
import com.geekandroid.sdk.sample.utils.FileUtils;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

/**
 * Created by lenovo on 2016/4/25.
 */
public class UpdateNotificationService extends Service {
    public String serverURL = "";
    private int serverVersionCode;
    private int localVersionCode;

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
