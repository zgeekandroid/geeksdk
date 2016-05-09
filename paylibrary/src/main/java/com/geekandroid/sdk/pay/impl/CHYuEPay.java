package com.geekandroid.sdk.pay.impl;


import com.geekandroid.sdk.commons.net.RequestCallBack;
import com.geekandroid.sdk.pay.IPay;

import java.util.Map;


public abstract class CHYuEPay extends IPay {
    @Override
    public void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) {
        throw  new IllegalStateException("不需要调用此方法");
    }
    @Override
    public void getPayResult(Map<String, Object> params, RequestCallBack<String> callBack) {
        throw  new IllegalStateException("不需要调用此方法");
    }

    @Override
    public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {

    }
}
