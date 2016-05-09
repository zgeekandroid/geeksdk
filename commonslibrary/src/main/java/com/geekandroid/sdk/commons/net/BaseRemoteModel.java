package com.geekandroid.sdk.commons.net;

import java.io.File;
import java.util.Map;

/**
 * date        :  2016-02-22  17:31
 * author      :  Mickaecle gizthon
 * description :
 */
public class BaseRemoteModel implements IRequestRemote<String> {


    @Override
    public void doGet(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        DefaultOkHttpIml.getInstance().doGet(url,parameters,callBack);
    }

    @Override
    public void doPost(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        DefaultOkHttpIml.getInstance().doPost(url,parameters,callBack);
    }


    @Override
    public void doUpload(String url, Map<String, Object> parameters, Map<String, File> map, RequestCallBack<String> callBack) {
        DefaultOkHttpIml.getInstance().doUpload(url, parameters, map, callBack);
    }

    @Override
    public void doDownLoad(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        DefaultOkHttpIml.getInstance().doDownLoad(url, parameters, callBack);
    }
}
