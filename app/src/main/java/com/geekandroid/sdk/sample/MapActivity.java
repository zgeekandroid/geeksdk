/*
package com.geekandroid.sdk.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.geekandroid.sdk.sample.map_navigationlibrary.base.BDBaseMapActivity;
import com.geekandroid.sdk.sample.map_navigationlibrary.location.Location;


*/
/**
 * 店家地图
 *
 * @author taozhishan 2015-5-14
 *//*

public class MapActivity extends BDBaseMapActivity implements View.OnClickListener {

    */
/**
     * 纬度
     *//*

    private double storeLatitude;
    */
/**
     * 经度
     *//*

    private double storeLongitude;
    */
/**
     * 店名
     *//*

    private String storeName;


    */
/**
     * 全局
     *//*

    private Context appContext;

    */
/**
     * 定位
     *//*

    private Button btn_locate;
    */
/**
     * 放大
     *//*

    private Button btn_zoom_in;
    */
/**
     * 缩小
     *//*

    private Button btn_zoom_out;


    private LinearLayout ll_search_route;
    private TextView tv_business_name;
    private TextView tv_address;
    private String storeAddress;

    private MapView mMapView;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initData();
        setBaseMap();
        setStorePopIcon();
    }

    @Override
    public void findViews() {
        mMapView = (MapView) findViewById(R.id.bmapView);

        btn_locate = (Button) findViewById(R.id.btn_locate);
        btn_zoom_in = (Button) findViewById(R.id.btn_zoom_in);
        btn_zoom_out = (Button) findViewById(R.id.btn_zoom_out);
        ll_search_route = (LinearLayout) findViewById(R.id.ll_search_route);
        tv_business_name = (TextView) findViewById(R.id.tv_business_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_locate.setOnClickListener(this);
        btn_zoom_in.setOnClickListener(this);
        btn_zoom_out.setOnClickListener(this);
        ll_search_route.setOnClickListener(this);
        btn_back.setOnClickListener(this);


        tv_address.setText(storeAddress);
        tv_business_name.setText(storeName);

    }

    */
/**
     * 初始数据
     *//*

    private void initData() {

        Intent intent = getIntent();
        try {
            storeLatitude = Double.valueOf(intent.getStringExtra("baiduLatitude"));
            storeLongitude = Double.valueOf(intent.getStringExtra("baiduLongitude"));
        } catch (Exception e) {

        }

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 定位
            case R.id.btn_locate:
                locate();
                break;
            // 放大
            case R.id.btn_zoom_in:
                zoomIn();
                break;
            // 缩小
            case R.id.btn_zoom_out:
                zoomOut();
                break;
            // 查看线路
            case R.id.ll_search_route:
                Intent intent = new Intent(this, RouteActivity.class);
                intent.putExtra("storeLatitude", storeLatitude);
                intent.putExtra("storeLongitude", storeLongitude);
                intent.putExtra("storeName", storeName);
                startActivity(intent);
                break;

            case R.id.btn_back:
                finish();
                break;
        }
    }


    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public Location getDefaultLocation() {
        Location location = new Location();
        location.setLatitude(storeLatitude);
        location.setLontitude(storeLongitude);
        storeName = location.getStreet();
        storeAddress =location.getAddress();

        return location;
    }

    @Override
    public LatLng getStoreLng() {
        return new LatLng(storeLatitude, storeLongitude);
    }

    @Override
    public int getMarkResId() {
        return R.mipmap.ic_map_red_label;
    }

    @Override
    public View getInfoWindow() {
        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.ic_map_pop_white);
        button.setTextSize(15);
        button.setText(storeName);
        button.getBackground().setAlpha(180);
        return button;
    }

}*/
