package com.geekandroid.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.geekandroid.sdk.sample.domain.VersionEvent;
import com.geekandroid.sdk.sample.update.domain.VersionBean;
import com.geekandroid.sdk.sample.utils.DialogUtils;
import com.jakewharton.rxbinding.view.RxView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //权限框架
        bindClick(R.id.rxpermissions, new RxPermissionsSampleFragment());

        bindClick(R.id.app_update, new AppUpdateSampleFragment());

    }


    public void jumpIntent(Fragment fragment) {
        ContentActivity.showFragment = fragment;
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }

    public void bindClick(int resId, Fragment fragment) {
        try {
            RxView.clicks(findViewById(resId)).subscribe(v -> {
                jumpIntent(fragment);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
