package com.geekandroid.sdk.jpushlibrary.push;

import android.content.Context;

import java.util.Set;

/**
 * date        :  2016-01-27  13:52
 * author      :  Mickaecle gizthon
 * description :
 */
public interface IPush {

    void init(Context context);

    void resumePush();

    void stopPush();

    void clearAllNotifications();

    void clearNotificationById(int id);

    void setAliasAndTags(String alias, Set<String> tags, IPushCallBack callback);

    void setAlias(String alias, IPushCallBack callback);

}
