package com.cample.thiredlibrary.third.share.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.cample.thiredlibrary.third.RequestCallBack;
import com.cample.thiredlibrary.third.share.IShare;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;


/**
 * date        :  2016-01-27  13:35
 * author      :  Mickaecle gizthon
 * description :友盟官方开发文档链接：http://dev.umeng.com/social/android/android-update
 */
public class WXShareImpl implements IShare {

    private String APPID;
    private String WXAPPSECRET;
    Activity activity;
    public  WXShareImpl(Activity activity){
        this.activity=activity;
//        PlatformConfig.setWeixin(APPID,WXAPPSECRET);
    }
    public void setConfig(String APPID,String WXAPPSECRET){
        this.APPID=APPID;
        this.WXAPPSECRET=WXAPPSECRET;
        PlatformConfig.setWeixin(APPID,WXAPPSECRET);
    }
    @Override
    public void doShare(String title, String content, String imageUrl, String targetUrl) {
        if (TextUtils.isEmpty(APPID)||TextUtils.isEmpty(WXAPPSECRET)){
            Toast.makeText( activity,"请先设置appid和secret",Toast.LENGTH_SHORT).show();
            return;
        }
        UMImage image= new UMImage(activity,imageUrl);
        new ShareAction(activity)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener)
                .withText(content)
                .withTargetUrl(targetUrl)
                .withMedia(image)
                .withTitle(title)
                .share();
    }
    RequestCallBack<String> callBack;
    public WXShareImpl addCallBack(RequestCallBack<String > callBack){
        this.callBack=callBack;
        return this;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            callBack.onSuccess("分享成功");

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            callBack.onFailure("分享失败",new Exception(throwable));
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            callBack.onCancel();
        }
    };

}
