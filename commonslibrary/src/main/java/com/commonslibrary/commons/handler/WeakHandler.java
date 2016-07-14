package com.commonslibrary.commons.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * date        :  2016-04-09  13:57
 * author      :  Mickaecle gizthon
 * description :
 */
public abstract class WeakHandler<T> extends Handler {
    private final WeakReference<T> weakReference;

    public WeakHandler(T reference) {
        this.weakReference = new WeakReference<>(reference);
    }

    @Override
    public final void handleMessage(Message msg) {
        T reference = weakReference.get();
        if (reference != null) {
            handleMessage(reference, msg);
        }
    }
    public abstract void handleMessage(T reference, Message msg);
}

