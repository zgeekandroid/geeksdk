package com.commonslibrary.commons.net;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.commonslibrary.commons.utils.DeviceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by gizthon on 16/9/7.
 * 必须支持rxjava的情况下才可以用此类
 */
public class BaseRxJavaRemoteModel implements IRxRequestRemote,IRequestRemote {

    private Context mContext;

    public BaseRxJavaRemoteModel() {

    }


    public void setTag(Object tag){
        DefaultRxJavaOkHttpIml.getInstance().setTag(tag);
    }

    public void cancelRquest(Object tag){
        DefaultRxJavaOkHttpIml.getInstance().cancelTag(tag);
    }

    public void cancelAllRequest(){
        DefaultRxJavaOkHttpIml.getInstance().cancelAllTag();
    }

    public BaseRxJavaRemoteModel(Object object) {

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


    public Map<String, Object> getExtraParameter() {
        Map<String, Object> parameters = new HashMap<>();

        if (mContext != null) {
            parameters.put("app_version", DeviceUtils.getAppVersionName(mContext));
            parameters.put("app_code", DeviceUtils.getAppVersionCode(mContext));
        }
        return parameters;
    }




    @Override
    public <T> void doGet(String url, Map<String, Object> parameters, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultRxJavaOkHttpIml.getInstance().doGet(url, parameters, callBack);
    }


    @Override
    public <T> void doPost(String url, Map<String, Object> parameters, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultRxJavaOkHttpIml.getInstance().doPost(url, parameters, callBack);
    }

    @Override
    public <T> void doUpload(String url, Map<String, Object> parameters, Map<String, File> map, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultRxJavaOkHttpIml.getInstance().doUpload(url, parameters, map, callBack);
    }

    @Override
    public <T> void doDownLoad(String url, Map<String, Object> parameters, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        DefaultRxJavaOkHttpIml.getInstance().doDownLoad(url, parameters, callBack);
    }

    @Override
    public <T> Observable<T> doRxGet(String url, Map<String, Object> parameters,Class<T> cls) {
        parameters.putAll(getExtraParameter());
        return DefaultRxJavaOkHttpIml.getInstance().doRxGet(url,parameters,cls);
    }

    @Override
    public <T> Observable<T> doRxPost(String url, Map<String, Object> parameters,Class<T> cls) {
        parameters.putAll(getExtraParameter());
        return DefaultRxJavaOkHttpIml.getInstance().doRxPost(url,parameters,cls);
    }

    @Override
    public <T> Observable<T> doRxUpload(String url, Map<String, Object> parameters, Map<String, File> files, RequestCallBack<T> callBack) {
        parameters.putAll(getExtraParameter());
        return DefaultRxJavaOkHttpIml.getInstance().doRxUpload(url,parameters,files,callBack);
    }
}
