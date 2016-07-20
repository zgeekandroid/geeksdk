package com.geekandroid.sdk.jpushlibrary.push.impl;

import android.content.Context;

import com.geekandroid.sdk.jpushlibrary.push.IPush;
import com.geekandroid.sdk.jpushlibrary.push.IPushCallBack;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;


/**
 * date        :  2016-01-27  13:53
 * author      :  Mickaecle gizthon
 * description :
 */
public class JPushImpl implements IPush {
    private static JPushImpl instance;
    private  Context context;
    private boolean isDebug=false;

    private JPushImpl() {
    }



    public static JPushImpl getInstance() {
        if (null == instance) {
            instance = new JPushImpl();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        this.context = context;
        JPushInterface.init(context);
        JPushInterface.setDebugMode(isDebug);
    }

    public void onResume(Context context){
        JPushInterface.onResume(context);
    }

    public void onPause(Context context){
        JPushInterface.onPause(context);
    }


public void setDebugMode(boolean isDebug){
    this.isDebug=isDebug;
}

    @Override
    public void resumePush() {
        JPushInterface.resumePush(context);
    }


    @Override
    public void stopPush() {
        try {
            JPushInterface.stopPush(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAllNotifications() {
        JPushInterface.clearAllNotifications(context);
    }

    @Override
    public void clearNotificationById(int id) {
        JPushInterface.clearNotificationById(context, id);
    }

    @Override
    public void setAliasAndTags(String alias, Set<String> tags, IPushCallBack callback) {
        JPushInterface.setAliasAndTags(context, alias, tags, callback);
    }

    @Override
    public void setAlias(String alias, IPushCallBack callback) {
        JPushInterface.setAlias(context, alias, callback);
    }




}
