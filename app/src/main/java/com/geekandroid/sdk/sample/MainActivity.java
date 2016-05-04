package com.geekandroid.sdk.sample;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;

        import com.geekandroid.sdk.sample.maplibrary.impl.BDLocationImpl;
        import com.jakewharton.rxbinding.view.RxView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BDLocationImpl.getInstance().init(getApplication());
        //权限框架
        bindClick(R.id.rxpermissions,new RxPermissionsSampleFragment());

        bindClick(R.id.common,new CommonSampleFragment());bindClick(R.id.location,new LocationSampleFragment());
    }


    public void jumpIntent(Fragment fragment){
        ContentActivity.showFragment = fragment;
        Intent intent = new Intent(this,ContentActivity.class);
        startActivity(intent);
    }

    public void bindClick(int resId,Fragment fragment){
        try {
            RxView.clicks(findViewById(resId)).subscribe(v->{
                jumpIntent(fragment);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
