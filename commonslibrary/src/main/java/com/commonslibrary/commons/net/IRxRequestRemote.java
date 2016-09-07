package com.commonslibrary.commons.net;


import com.commonslibrary.commons.net.RequestCallBack;

import java.io.File;
import java.util.Map;

/**
 * model request interface , the interface request from server
 */
public interface IRxRequestRemote {
    /**
     * net work request
     *
     * @param url        request url
     * @param parameters request parameters
     */
    <T> rx.Observable<T> doRxGet(String url, Map<String, Object> parameters,Class<T> cls);

    <T> rx.Observable<T> doRxPost(String url, Map<String, Object> parameters,Class<T> cls);

    <T> rx.Observable<T> doRxUpload(String url, Map<String, Object> parameters, Map<String, File> files, RequestCallBack<T> callBack);


}
