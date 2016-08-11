package com.bluetoothlibrary.base;

import android.app.Application;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppInfo.init(getApplicationContext());
    }

}
