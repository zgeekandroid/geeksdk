package com.cample.thiredlibrary.third.login.impl;

import android.app.Activity;
import android.content.Intent;

import com.cample.thiredlibrary.third.RequestCallBack;
import com.cample.thiredlibrary.third.login.ILogin;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * date        :  2016-01-27  14:00
 * author      :  Mickaecle gizthon
 * description :友盟官方开发文档链接：http://dev.umeng.com/social/android/android-update
 */
public class QQLoginImpl implements ILogin {
    private Activity activity;
    private UMShareAPI api;
    public QQLoginImpl(Activity activity){
        this.activity=activity;
        PlatformConfig.setQQZone("","");
        api = UMShareAPI.get(activity.getApplicationContext());

    }

    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        api.onActivityResult(arg0, arg1, arg2);
    }
    RequestCallBack<Map>callBack;

    public QQLoginImpl addCallBack(RequestCallBack<Map > callBack){
        this.callBack=callBack;
        return this;    }
    public  void doOauth(){

        api.doOauthVerify(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                getUserInfo();
            }


            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                callBack.onFailure("失败",new Exception(throwable));
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                callBack.onCancel();
            }
        });
    }
    private  void getUserInfo(){
        api.getPlatformInfo(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                callBack.onSuccess(map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                callBack.onFailure("失败",new Exception(throwable));
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                callBack.onCancel();
            }
        });
    }
    private boolean isInstallQQ(){

        return api.isInstall(activity,SHARE_MEDIA.QQ);
    }
    @Override
    public void login() {
        if (isInstallQQ()){
            doOauth();
        }else {
            callBack.onFailure("未安装QQ客户端",new Exception());
        }


    }

}
