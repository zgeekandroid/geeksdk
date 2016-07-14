package com.commonslibrary.commons.net;


import java.io.File;
import java.util.Map;

/**
 * model request interface , the interface request from server
 */
public interface IRequestRemote<T> {
    /**
     * net work request
     *
     * @param url        request url
     * @param parameters request parameters
     * @param callBack   callback
     */
    void doGet(String url, Map<String, Object> parameters, RequestCallBack<T> callBack);

    void doPost(String url, Map<String, Object> parameters, RequestCallBack<T> callBack);

    void doUpload(String url, Map<String, Object> parameters, Map<String, File> map, RequestCallBack<T> callBack);

    void doDownLoad(String url, Map<String, Object> parameters, RequestCallBack<T> callBack);

}
