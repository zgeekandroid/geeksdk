package com.commonslibrary.commons.net;

import java.io.Reader;

/**
 * date        :  2016-02-02  15:24
 * author      :  Mickaecle gizthon
 * description :
 */
public abstract class RequestCallBack<T> {

    public void onStart() {
    }

    public abstract void onSuccess(T result);

    public void onSuccess(Reader reader) {
    }

    public abstract void onFailure(String errorMessage, Exception exception);

    public void onCancel() {
    }

    public void onProgress(long byteWrite, long contentLength, boolean isDone) {
    }

    ;

}
