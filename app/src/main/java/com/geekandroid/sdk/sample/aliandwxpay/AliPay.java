package com.geekandroid.sdk.sample.aliandwxpay;


import com.geekandroid.sdk.commons.net.RequestCallBack;
import com.geekandroid.sdk.pay.impl.CHAlipay;
import com.geekandroid.sdk.pay.impl.PayResult;
import com.geekandroid.sdk.pay.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *@author:BingCheng
 *@date:2016/4/28 17:49
 */
public class AliPay extends CHAlipay {


 private String alipayPublicKey = "";
 private final String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPDVgY4DAq6dfXO0k/fy0mWldPkhUiiFYNgFF8Xd4Jxl3wayc1/4abl8RIfgt8ReUU3cjRUR0ZVh98yb4J/DeH4heLKcLJ870qxoJHCSQ0vac54eYdJyRaYyveaWJIhb23kVUxUq0ZahPZzELZP8aut85CTITtF4yT6FjzWIYNatAgMBAAECgYEAhSUEowZ3DSJ0oOtMgt/0Ac4fhTwut8hFaigmpHWuH8kSLBmsP3qOuMVC/fv2nNGoEWvFc8iOHhAzdyMWZD0k90POwasmuWLiNEJon7S2G9kk361dZTztQMdQmS331frd73Jt8Mo+QfoEA5KAfrNr3ZIHKi+TPoBJtwK8HbQSMQECQQD/uIrFATVFWvq/eyQQO/fkq8sotQlBTdRfgTQkQVtsFfH8F5gpdUuo0vvwAh03PhGkI1qJTYAuo9forAJY+MWFAkEA8RjN1v9K610FOeUku8F1p26tzqMaD8U/DhHKl0yt2aSOuhA5ZovbVRMWsW9XbYs9dTx+Hclto9lhUxnMtGjhCQJAd9IMk8rBH0JGA7q7Zy/yM5+bjfNl5seFH0r3F5XYNIkD3c9gaOyhCvXxPsB+2AepVHOMVJGP4kq7PDZ80xud0QJALIcwPS0ZYS7k8DRp/GNWn33JjAiDn7YGV0rh59+5nCDQW3zznQTnlWbZIhw2QX0d9LEfedDh9wkG+JojPI7CQQJAUivCBfuJcASz7AJgvb5VK3zrvKQHEZU/aQgSCVpxoijL2xyEtd5tt5qSfyMJA3IKuLhxBgv4PrSWcgQrsIwFrw==";
 private Map<String, Object> parameters = new HashMap<>();
 private RequestCallBack<String> callBack;
 //返回支付结果
    @Override
    public void callClientSuccess(PayResult result) {
     String resultInfo = result.getResult();
     String content = getSignContent(resultInfo);
     String sign = getSign(resultInfo);
     boolean verify = SignUtils.verify(content, sign, alipayPublicKey);
     // 验签不正确
     if (!verify) {
      hideProgress("支付数据异常，请重试");
      return;
     }
     hideProgress();
     if (callBack == null) {
      return;
     }
     callBack.onSuccess("支付成功");   
    }
   //调用的支付方法
    @Override
    public void pay(Map<String, Object> params, RequestCallBack<String> callBack) {
     if (activity == null) {
      throw new NullPointerException("没有初始化支付");
     }
     //给private_key赋值
     setPrivate_key(private_key);

     //显示进度条
     showProgress();

     this.parameters = params;

     getPayParam(parameters, getParamCallBack);
     
     doRealPay(parameters);
     this.callBack = callBack;
    }
    //获取约定参数
    @Override
    public void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) {
     //1.从服务器获取支付参数
    }
 
 
 private RequestCallBack<String> getParamCallBack = new RequestCallBack<String>() {
  @Override
  public void onSuccess(String result) {
   //从这里获取到支付的notify url  和 ip
//   AliParameterEntity parameterEntity = presenter.fromJson(result, AliParameterEntity.class);
//
   Map<String, Object> parme = new HashMap<>();
//   parme.put("subject", parameters.get("subject"));
//   parme.put("body", parameters.get("body"));
//   parme.put("out_trade_no", parameters.get("out_trade_no"));
//   parme.put("total_fee", parameters.get("total_fee"));
//   parme.put("seller_id", parameterEntity.getBackinfo().getAlipaySellerId());
//   parme.put("partner", parameterEntity.getBackinfo().getAlipayPartner());
//   boolean isRecharge = false;
//   if (parameters.containsKey("isRecharge")) {
//    isRecharge = (boolean) parameters.get("isRecharge");
//   }
//   if (isRecharge) {
//    parme.put("notify_url", parameterEntity.getBackinfo().getAlipayNotifyUrlRecharge());
//   } else {
//    parme.put("notify_url", parameterEntity.getBackinfo().getAlipayNotifyUrlPay());
//   }
//   alipayPublicKey = parameterEntity.getBackinfo().getAlipayPublicKey();
//   parme.put("spbillCreateIp", parameterEntity.getBackinfo().getIp());
   doRealPay(parme);


  }

  @Override
  public void onFailure(String errorMessage, Exception exception) {
   hideProgress(errorMessage);
  }
 };
}
