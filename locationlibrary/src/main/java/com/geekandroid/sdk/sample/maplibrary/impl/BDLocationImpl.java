package com.geekandroid.sdk.sample.maplibrary.impl;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.geekandroid.sdk.sample.commonslibrary.config.SystemConfig;
import com.geekandroid.sdk.sample.commonslibrary.net.RequestCallBack;
import com.geekandroid.sdk.sample.commonslibrary.utils.LogUtils;
import com.geekandroid.sdk.sample.maplibrary.ILocation;
import com.geekandroid.sdk.sample.maplibrary.Location;


/**
 * date        :  2016-02-16  12:55
 * author      :  Mickaecle gizthon
 * description :  http://lbsyun.baidu.com/index.php?title=android-locsdk
 */
public class BDLocationImpl implements ILocation, BDLocationListener {

    private BDLocationService locationService;
    private static BDLocationImpl instance;


    private BDLocationImpl() {
    }

    public static BDLocationImpl getInstance() {
        if (null == instance) {
            instance = new BDLocationImpl();
        }
        return instance;
    }

    public BDLocationService getLocationService() {
        return locationService;
    }

    @Override
    public void init(Context context) {
        if (context != null){
            locationService = new BDLocationService(context.getApplicationContext());
            try {
                SDKInitializer.initialize(SystemConfig.getSystemBaiduDir(),context.getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    SDKInitializer.initialize( context.getApplicationContext());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    @Override
    public void start() {
        locationService.registerListener(this);
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }


    public void start(RequestCallBack<Location> callBack) {
        locationService.registerListener(this);
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        setCallBack(callBack);
    }

    @Override
    public void stop() {
        locationService.unregisterListener(this); //注销掉监听
        locationService.stop(); //停止定位服务
    }


    private RequestCallBack<Location> callBack;

    public void setCallBack(RequestCallBack<Location> callBack){
        this.callBack = callBack;
    }



    private void callBackSuccess(Location location){
        if (callBack != null){
            callBack.onSuccess(location);
        }
    }

    private void callBackFailed(String errorMessage){
        if (callBack != null){
            callBack.onFailure(errorMessage, new Exception(""));
        }
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {

            Location location = new Location();

            location.setTime(bdLocation.getTime());
            location.setErrorCode(bdLocation.getLocType());
            location.setLatitude(bdLocation.getLatitude());
            location.setLontitude(bdLocation.getLongitude());
            location.setRadius(bdLocation.getRadius());
            location.setCountry(bdLocation.getCountry());
            location.setCountryCode(bdLocation.getCountryCode());
            location.setCity(bdLocation.getCity());
            location.setCityCode(bdLocation.getCityCode());
            location.setDirection(bdLocation.getDirection());
            location.setDistrict(bdLocation.getDistrict());
            location.setAddress(bdLocation.getAddrStr());
            location.setStreet(bdLocation.getStreet());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                location.setSpeed(bdLocation.getSpeed());
                location.setSatellite(bdLocation.getSatelliteNumber());
                location.setHeight(bdLocation.getAltitude());

                callBackSuccess(location);
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                callBackSuccess(location);
            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                callBackSuccess(location);
            } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                callBackFailed("服务端网络定位失败");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                callBackFailed("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                callBackFailed("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

            //打印日志
            logMessage(bdLocation);
        }
    }

    private void logMessage(BDLocation bdLocation) {
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        /**
         * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
         * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
         */
        sb.append(bdLocation.getTime());
        sb.append("\nerror code : ");
        sb.append(bdLocation.getLocType());
        sb.append("\nlatitude : ");
        sb.append(bdLocation.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(bdLocation.getLongitude());
        sb.append("\nradius : ");
        sb.append(bdLocation.getRadius());
        sb.append("\nCountryCode : ");
        sb.append(bdLocation.getCountryCode());
        sb.append("\nCountry : ");
        sb.append(bdLocation.getCountry());
        sb.append("\ncitycode : ");
        sb.append(bdLocation.getCityCode());
        sb.append("\ncity : ");
        sb.append(bdLocation.getCity());
        sb.append("\nDistrict : ");
        sb.append(bdLocation.getDistrict());
        sb.append("\nStreet : ");
        sb.append(bdLocation.getStreet());
        sb.append("\naddr : ");
        sb.append(bdLocation.getAddrStr());
        sb.append("\nDescribe: ");
        sb.append(bdLocation.getLocationDescribe());
        sb.append("\nDirection(not all devices have value): ");
        sb.append(bdLocation.getDirection());
        sb.append("\nPoi: ");

        if (bdLocation.getPoiList() != null && !bdLocation.getPoiList().isEmpty()) {
            for (int i = 0; i < bdLocation.getPoiList().size(); i++) {
                Poi poi = (Poi) bdLocation.getPoiList().get(i);
                sb.append(poi.getName() + ";");
            }
        }

        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(bdLocation.getSpeed());// 单位：km/h
            sb.append("\nsatellite : ");
            sb.append(bdLocation.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(bdLocation.getAltitude());// 单位：米
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");
        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            // 运营商信息
            sb.append("\noperationers : ");
            sb.append(bdLocation.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
        } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
        } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }

        LogUtils.i(sb.toString());
    }
}
