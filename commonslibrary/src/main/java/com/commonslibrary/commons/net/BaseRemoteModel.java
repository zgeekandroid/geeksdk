package com.commonslibrary.commons.net;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import com.commonslibrary.commons.utils.DeviceUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * date        :  2016-02-22  17:31
 * author      :  Mickaecle gizthon
 * description :
 */
public  class BaseRemoteModel implements IRequestRemote<String> {
    
    private Context mContext;
    
    public BaseRemoteModel(){
        
    }
    public   BaseRemoteModel(Object object){
        
            if (object instanceof android.app.Fragment) {
                android.app.Fragment fragment = (android.app.Fragment) object;
                mContext = fragment.getActivity();

            } else if (object instanceof android.support.v4.app.Fragment) {
                android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) object;
                mContext = fragment.getActivity();
            } else if (object instanceof Activity) {
                mContext = (Activity) object;
            } else if (object instanceof Application) {
                mContext = ((Application) object).getApplicationContext();
            } 
        
    }

    public  Map<String, Object> getExtraParameter(){
        Map<String, Object>    parameters =      new HashMap<>();

        if (mContext != null){
            parameters.put("app_version",  DeviceUtils.getAppVersionName(mContext));
            parameters.put("app_code", DeviceUtils.getAppVersionCode(mContext));
        }
        return  parameters;
    }

    @Override
    public void doGet(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doGet(url,parameters,callBack);
    }

    @Override
    public void doPost(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doPost(url,parameters,callBack);
    }


    @Override
    public void doUpload(String url, Map<String, Object> parameters, Map<String, File> map, RequestCallBack<String> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doUpload(url, parameters, map, callBack);
    }

    @Override
    public void doDownLoad(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultOkHttpIml.getInstance().doDownLoad(url, parameters, callBack);
    }
}
