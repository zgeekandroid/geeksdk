package com.geekandroid.sdk.sample.maplibrary;

import android.content.Context;

/**
 * date        :  2016-02-16  12:50
 * author      :  Mickaecle gizthon
 * description :  通用接口
 */
public interface ILocation {
    void init(Context context);
    void start();
    void stop();

}
