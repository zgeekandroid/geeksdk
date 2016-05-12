package com.cample.thiredlibrary.third.login.impl;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

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
public class WXLoginImpl implements ILogin {
    private String APPID="";
    private String WXAPPSECRET="";
    @Override
    public void login() {
        if (isInstallWeixin()){
            doOauth();
        }else {
            callBack.onFailure("未安装微信客户端",new Exception());
        }
    }
    public void setConfig(String APPID,String WXAPPSECRET){
        this.APPID=APPID;
        this.WXAPPSECRET=WXAPPSECRET;
        PlatformConfig.setWeixin(APPID,WXAPPSECRET);
    }
    private Activity activity;
    private UMShareAPI api;
    public WXLoginImpl(Activity activity){
        this.activity=activity;
//        PlatformConfig.setWeixin("","");
        api = UMShareAPI.get(activity.getApplicationContext());
    }
    public boolean isInstallWeixin(){
        return api.isInstall(activity, SHARE_MEDIA.WEIXIN);
    }
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        api.onActivityResult(arg0, arg1, arg2);
    }


    private  void doOauth(){
        if (TextUtils.isEmpty(APPID)||TextUtils.isEmpty(WXAPPSECRET)){
            Toast.makeText(activity,"请先设置appid和secret",Toast.LENGTH_SHORT).show();
            return;
        }
        api.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
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
        api.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, listener);
    }
    RequestCallBack<Map> callBack;

    public WXLoginImpl addCallBack(RequestCallBack<Map > callBack){
        this.callBack=callBack;
        return this;
    }
    UMAuthListener listener=new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //map ->  json
            callBack.onSuccess(map);

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            callBack.onFailure("获取信息失败",new Exception(throwable));

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            callBack.onCancel();
        }
    };


}
