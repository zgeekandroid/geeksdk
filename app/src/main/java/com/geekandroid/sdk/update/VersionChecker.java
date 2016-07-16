package com.geekandroid.sdk.update;

import android.content.Context;

import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.DeviceUtils;
import com.commonslibrary.commons.utils.EncryptionUtils;
import com.geekandroid.sdk.update.domain.VersionBean;
import com.geekandroid.sdk.update.net.NetUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/25.
 */
public class VersionChecker {
    private static int serverVersionCode;
    private static int localVersionCode;

    public static final String securityCode = "txsccl8457210%#&@4";

    public static void getServerVersionCode(Context context, int versionCode, RequestCallBack<String> callback) {
        String serverURL = "http://data.maicaim.com/Common/APPUpdate.ashx";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("port_password", generatePortPasswordNoLogin("200" + versionCode));
        parameters.put("app_type", "200");
        parameters.put("version", versionCode + "");
        NetUtils.doPost(serverURL, parameters, callback);
    }

    public static int getLocalVersionCode(Context context) {
        localVersionCode = DeviceUtils.getAppVersionCode(context);
        return localVersionCode;
    }


    public static int getIsNeedUpdateCode(VersionBean versionBean) {
        return versionBean.getBackinfo().getApp_is_update();
    }

    private static boolean isNeedUpdate(VersionBean versionBean) {
        return getIsNeedUpdateCode(versionBean) == 1 ? true : false;
    }

    public static String generatePortPasswordNoLogin(String encryptionFiled) {
        return EncryptionUtils.md5(securityCode + encryptionFiled);
    }
}
