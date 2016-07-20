package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.view.View;

import com.geekandroid.sdk.jpushlibrary.push.IPushCallBack;
import com.geekandroid.sdk.jpushlibrary.push.impl.JPushImpl;

import java.util.Set;

/**
 * date        :  2016-02-04  18:37
 * author      :  Mickaecle gizthon
 * description :
 */
public class JPushSampleActivity extends BaseActivity {
    //        http://docs.jpush.io/guideline/android_guide/
    //        http://docs.jpush.io/client/android_api/#api_3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush);

        findViewById(R.id.resumePush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPushImpl.getInstance().resumePush();
                logI("resumePush");
            }
        });
        findViewById(R.id.stopPush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPushImpl.getInstance().stopPush();
                logI("stopPush");

            }
        });
        findViewById(R.id.setAlias).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JPushImpl.getInstance().setAlias("3", new IPushCallBack() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        logI(" got Result : i = " + i + " ,s = " + s + " ,set = " + set);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
