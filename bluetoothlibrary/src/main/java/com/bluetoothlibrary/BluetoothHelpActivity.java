package com.bluetoothlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


/**
 * 打印界面
 */
public class BluetoothHelpActivity extends Activity {

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_help);
        initView();
        initListener();
    }

    private void initView() {
        iv_back=(ImageView) findViewById(R.id.iv_back);
        
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
