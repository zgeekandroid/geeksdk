package com.geekandroid.sdk.sample.update;

import android.content.Context;
import android.util.Log;

import com.geekandroid.sdk.sample.update.domain.VersionBean;
import com.geekandroid.sdk.sample.update.net.EncryptionUtils;
import com.geekandroid.sdk.sample.update.net.NetUtils;
import com.geekandroid.sdk.sample.update.net.RequestCallBack;
import com.geekandroid.sdk.sample.utils.DeviceUtils;
import com.geekandroid.sdk.sample.utils.LogUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/25.
 */
public class VersionChecker {
    private static int serverVersionCode;
    private static int localVersionCode;

    public static final String securityCode = "txsccl8457210%#&@4";

    public static int getServerVersionCode(Context context, int versionCode) {
        String serverURL = "http://data.maicaim.com/Common/APPUpdate.ashx";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("port_password", generatePortPasswordNoLogin("200" + versionCode));
        parameters.put("app_type", "200");
        parameters.put("version", versionCode + "");
        NetUtils.doPost(serverURL, parameters, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                VersionBean versionBean = gson.fromJson(result, VersionBean.class);
                String app_version_new = versionBean.getBackinfo().getApp_version_new();
                if (null != app_version_new) {
                    serverVersionCode = Integer.parseInt(app_version_new);
                } else {
                    serverVersionCode = -1;
                }

            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                serverVersionCode = -1;
            }
        });
        return serverVersionCode;
    }

    public static int getLocalVersionCode(Context context) {
        localVersionCode = DeviceUtils.getAppVersionCode(context);
        return localVersionCode;
    }

    public static String generatePortPasswordNoLogin(String encryptionFiled) {
        return EncryptionUtils.md5(securityCode + encryptionFiled);
    }
}
