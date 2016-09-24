/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.maplibrary.impl;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.geekandroid.sdk.maplibrary.ILocation;
import com.geekandroid.sdk.maplibrary.Location;

import java.io.Reader;
import java.util.ArrayList;


/**
 * date        :  2016-02-16  12:55
 * author      :  Mickaecle gizthon
 * description :  http://lbsyun.baidu.com/index.php?title=android-locsdk
 */
public class BDLocationImpl implements ILocation, BDLocationListener {

    private BDLocationService locationService;
    private static BDLocationImpl instance;

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    //是否持续的请求定位
    private boolean isInterrupt = true;
    private BDLocationImpl() {
    }

    public static BDLocationImpl getInstance() {
        if (null == instance) {
            instance = new BDLocationImpl();
        }
        return instance;
    }

    public boolean isInterrupt() {
        return isInterrupt;
    }

    public void setInterrupt(boolean interrupt) {
        isInterrupt = interrupt;
    }

    public BDLocationService getLocationService() {
        return locationService;
    }

    @Override
    public void init(Context context) {
        if (context != null){
            locationService = new BDLocationService(context.getApplicationContext());
            locationService.registerListener(this);
        }

    }

    @Override
    public void start(Activity activity) {
        start(activity,null);
    }

    @TargetApi(23)
    private void getPersimmions( ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(activity,permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(activity,permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                activity.requestPermissions(permissions.toArray(new String[permissions.size()]),  SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(Activity context,ArrayList<String> permissionsList, String permission) {
        if ( context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if ( context.shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    private Activity activity;


    public void start(Activity activity,RequestCallBack<Location> callBack) {
        this.activity = activity;
        getPersimmions();
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        if (callBack != null){
            setCallBack(callBack);
        }
    }

    @Override
    public void stop() {
        if (locationService != null){
            locationService.unregisterListener(this); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        callBack = null;
    }

    public void onPause(){
        if (locationService != null){
            locationService.stop(); //停止定位服务
        }
        if (activity != null){
            activity = null;
        }
    }

    public interface  RequestCallBack<T> {


        public abstract void onSuccess(T result);

        public abstract void onFailure(String errorMessage, Exception exception);

    }
    private RequestCallBack<Location> callBack;

    public void setCallBack(RequestCallBack<Location> callBack){
        this.callBack = callBack;
    }



    private void callBackSuccess(Location location){
        if (callBack != null){
            callBack.onSuccess(location);
            if (isInterrupt){
                onPause();
            }
        }
    }

    private void callBackFailed(String errorMessage){
        if (callBack != null){
            callBack.onFailure(errorMessage, new Exception(""));
            if (isInterrupt){
                onPause();
            }
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
            location.setProvince(bdLocation.getProvince());
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
        StringBuffer sb = new StringBuffer(500);
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
        sb.append("\nProvince : ");
        sb.append(bdLocation.getProvince());
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

        Log.i("BDLocationImpl",sb.toString());
    }
}
