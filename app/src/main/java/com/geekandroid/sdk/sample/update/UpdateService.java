package com.geekandroid.sdk.sample.update;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
        serverVersionCode = VersionChecker.getServerVersionCode(this, localVersionCode);
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
}
