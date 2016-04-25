package com.geekandroid.sdk.sample.update.net;

import java.util.Map;

/**
 * Created by lenovo on 2016/4/25.
 */
public class NetUtils<T> {
    static IRequestRemote mIRequestRemote = DefaultOkHttpIml.getInstance();

    public static void doGet(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        mIRequestRemote.doGet(url, parameters, callBack);
    }

    public static void doPost(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        mIRequestRemote.doPost(url, parameters, callBack);
    }
}
