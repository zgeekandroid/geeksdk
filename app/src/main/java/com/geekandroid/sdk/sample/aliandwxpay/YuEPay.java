package com.geekandroid.sdk.sample.aliandwxpay;


import com.geekandroid.sdk.commons.net.BaseRemoteModel;
import com.geekandroid.sdk.commons.net.RequestCallBack;
import com.geekandroid.sdk.pay.impl.CHYuEPay;

import java.util.Map;

/**
 * date        :  2016-04-02  17:46
 * author      :  Mickaecle gizthon
 * description :
 */
public class YuEPay extends CHYuEPay {
    private BaseRemoteModel model = new BaseRemoteModel();

    //开启支付流程，请求订单 -> 获得请求参数 -> 调用客户端支付 -> 回调查询支付
    @Override
    public void pay(Map<String, Object> params, final RequestCallBack<String> callBack) {
        if (activity == null) {
            throw new NullPointerException("没有初始化支付");
        }
        showProgress();
        String url = params.get("url").toString();
        params.remove("url");//移除掉url


        model.doPost(url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                hideProgress();
                if (callBack == null) {
                    return;
                }
                callBack.onSuccess(result);
            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                hideProgress(errorMessage);
                if (callBack == null) {
                    return;
                }
                callBack.onFailure(errorMessage, exception);
            }
        });

    }



}
