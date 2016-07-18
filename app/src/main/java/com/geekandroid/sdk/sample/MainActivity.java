package com.geekandroid.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.geekandroid.sdk.maplibrary.impl.BDLocationImpl;
import com.imagerloaderlibrary.imagerloader.ImageLoaderManager;
import com.jakewharton.rxbinding.view.RxView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BDLocationImpl.getInstance().init(getApplication());

        // 初始化ImageLoader
        ImageLoaderManager.getInstance().init(this);
        //权限框架
        bindClick(R.id.rxpermissions, new RxPermissionsSampleFragment());

        bindClick(R.id.common, new CommonSampleFragment());
//        bindClick(R.id.mapnavigation, MapActivity.class);
        bindClick(R.id.location, new LocationSampleFragment());
        bindClick(R.id.pay, new PaySampleFragment());
        bindClick(R.id.imageloader, new ImageloaderFragment());

        bindClick(R.id.qrcode, MipcaActivityCapture.class);
//        bindClick(R.id.image_crop, CropSampleActivity.class);
        bindClick(R.id.app_update, new AppUpdateSampleFragment());
//        bindClick(R.id.citylist, CityListActivity.class);

 
        Button image_crop = (Button) findViewById(R.id.image_crop);
        Button image_preview = (Button) findViewById(R.id.image_preview);

        image_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CropSampleActivity.class);
//                startActivity(intent);
            }
        });
        image_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PreviewMainActivity.class);
//                startActivity(intent);
            }
        });

 
    }


    public void jumpIntent(Fragment fragment) {
        ContentActivity.showFragment = fragment;
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }

    public void jumpIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
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

    public void bindClick(int resId, Class<?> cls) {
        try {
            RxView.clicks(findViewById(resId)).subscribe(v -> {
                jumpIntent(cls);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
