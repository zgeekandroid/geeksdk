package com.geekandroid.sdk.maplibrary;

import android.app.Activity;
import android.content.Context;

/**
 * date        :  2016-02-16  12:50
 * author      :  Mickaecle gizthon
 * description :  通用接口
 */
public interface ILocation {
    void init(Context context);
    void start(Activity activity);
    void stop();
}
