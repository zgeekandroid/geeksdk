
/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.sample.wxapi;


import com.commonslibrary.commons.net.BaseRemoteModel;
import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.ToastUtils;
import com.geekandroid.sdk.pay.impl.CHWeiXinPay;

import java.util.HashMap;
import java.util.Map;


/**
 * date        :  2016-04-02  17:46
 * author      :  Mickaecle gizthon
 * description :
 */

public class WeiXinPay extends CHWeiXinPay {
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    //微信支付appid
    public static String APP_ID = "wx8a430a43a5b54083";
    //微信支付商户号
    public static String MCH_ID = "1330457301";
    //  API密钥，在商户平台设置
    public static String API_KEY = "fdsafoij233foefhFKJDo2qwuf234k33";
    
//    private BasePresenter presenter = new BasePresenter();
    private BaseRemoteModel model = new BaseRemoteModel();
    private Map<String, Object> parameters = new HashMap<>();
    private RequestCallBack<String> callBack;
    public WeiXinPay() {
    }

    //初始化

    //开启支付流程，请求订单 -> 获得请求参数 -> 调用客户端支付 -> 回调查询支付
    @Override
    public void pay(Map<String, Object> params, RequestCallBack<String> callBack) {
        if (activity == null) {
            throw new NullPointerException("没有初始化支付");
        }

        if (!msgApi.isWXAppInstalled()) {
            ToastUtils.show(activity, "没有安装微信客户端,请先安装!");
            return;
        }

        setKeyAndID(API_KEY, APP_ID, MCH_ID);
        showProgress();

        this.callBack = callBack;
        this.parameters = params;

        requestOrder(parameters,requestCallBack);

        getPayParam(parameters, getParamCallBack);
    }


    @Override
    public void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) {
        String userIdKey = "user_id";
        if (!params.containsKey(userIdKey)) {
            throw new IllegalArgumentException("获取支付参数不正确,没有user_id");
        }
//        String url = presenter.generateUrl("Purchase/Pay/weixinpay/GetParameterWeixin.ashx");
        Map<String, Object> parameters = new HashMap<>();
        //参数转换
        String userid = params.get(userIdKey).toString();
        parameters.put(userIdKey, userid);
//        parameters.put("port_password", presenter.generatePortPassword(userid));
//        model.doPost(url, parameters, callBack);
    }

    @Override
    public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {
    }


    private RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(String result) {
            getPayParam(parameters, getParamCallBack);
        }

        @Override
        public void onFailure(String errorMessage, Exception exception) {
            hideProgress(errorMessage);
        }
    };




    private RequestCallBack<String> getParamCallBack = new RequestCallBack<String>() {
        @Override
        public void onSuccess(String result) {

        //从这里获取到支付的notify url  和 ip
//            WeixinParameterEntity parameterEntity = presenter.fromJson(result, WeixinParameterEntity.class);
            Map<String, Object> para = new HashMap<>();
//            para.put("detail",parameters.get("detail"));
//            para.put("body",parameters.get("body"));
//            para.put("out_trade_no",parameters.get("out_trade_no"));
//            para.put("total_fee",parameters.get("total_fee"));
//            para.put("attach",parameters.get("attach"));
//            para.put("spbill_create_ip",parameterEntity.getBackinfo().getIp());
//            para.put("notify_url", parameterEntity.getBackinfo().getWeixinNotifyurl());


            doRealPay(para);
        }

        @Override
        public void onFailure(String errorMessage, Exception exception) {
            hideProgress(errorMessage);
        }
    };


    @Override
    public void callClientSuccess(  boolean isSuccess) {
        //调用微信端成功
        //此处在微信客户端中，调用了微信  xx.xx.xx.wxapi.WXPayEntryActivity.java 进入这个类中，进行查询是否支付成功
        if (callBack == null){
            return;
        }
        if (isSuccess){
            //打开微信客户端
            callBack.onSuccess("");
        }else {
            callBack.onFailure("请升级最新版微信使用",new Exception(""));
        }
    }
}

