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

package com.geekandroid.sdk.pay.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.commonslibrary.commons.handler.WeakHandlerNew;
import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.ArithUtils;
import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.MD5;
import com.geekandroid.sdk.pay.IPay;
import com.geekandroid.sdk.pay.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public abstract class CHWeiXinPay extends IPay {

    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    //微信支付appid
    private static String APP_ID = "";
    //微信支付商户号
    private static String MCH_ID = "";
    //  API密钥，在商户平台设置
    private static String API_KEY = "";
    private static String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
    protected IWXAPI msgApi;


    private WeakHandlerNew handler = new WeakHandlerNew(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            handleRealMessage(msg);
            return false;
        }
    });


    public void setKeyAndID(String apiKey, String appid, String mchid) {
        API_KEY = apiKey;
        APP_ID = appid;
        MCH_ID = mchid;
    }

    public void registerApp() {
        msgApi.registerApp(APP_ID);
    }

    @Override
    public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {

    }

    @Override
    public void init(Activity activity) {
        super.init(activity);
        msgApi = WXAPIFactory.createWXAPI(activity, null);
        if (msgApi.isWXAppInstalled()) {
            registerApp();
        } else {
            LogUtils.e("没有安装微信");
        }

    }

    public void doRealPay(final Map<String, Object> parameters) {

        if (TextUtils.isEmpty(APP_ID) || TextUtils.isEmpty(MCH_ID) || TextUtils.isEmpty(API_KEY)) {
            LogUtils.e("必须指定APP_ID ， MCH_ID，API_KEY");
            return;
        }
        //check request
        if (isEmpty(parameters.get("body")) ||
                isEmpty(parameters.get("out_trade_no")) ||
                isEmpty(parameters.get("total_fee")) ||
                isEmpty(parameters.get("spbill_create_ip")) ||
                isEmpty(parameters.get("notify_url"))) {

            LogUtils.e("必须指定参数：[ detail, out_trade_no, spbill_create_ip, total_fee, notify_url]");
            return;
        }

        if (parameters.get("out_trade_no").toString().length() > 32) {
            LogUtils.e("订单长度不能超过 32 位");
            return;
        }

        //微信支付的金额转换，将传过来的 “元” 转为  “分” 如果传入的是double 类型，就将其转换成分，因为微信支付的单位是分
        if (parameters.get("total_fee") instanceof Double){
            String totalfee = parameters.get("total_fee").toString();
            int realMoney = (int) ArithUtils.mul(Double.parseDouble(totalfee), 100);
            parameters.put("total_fee", realMoney);
        }


        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = pay(parameters);
                Message msg = new Message();
                msg.what = WX_SDK_PAY_FLAG;
                msg.obj = isSuccess;
                handler.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * Handler 中支付成功的调用
     */
    public abstract void callClientSuccess(boolean isSuccess);


    @Override
    public void getPayResult(Map<String, Object> params, RequestCallBack<String> callBack) {

    }

    protected void handleRealMessage(Message msg) {
        if (msg.what == WX_SDK_PAY_FLAG) {
            boolean isSuccess = (boolean) msg.obj;

            hideProgress();
            callClientSuccess(isSuccess);
        }

    }

    /**
     * @param parameters 字段名  |  变量名  |  必填  |  类型  |  示例值  |  描述
     *                   应用ID  |  appid  |  是  |  String(32)  |  wxd678efh567hg6787  |  微信开放平台审核通过的应用APPID  |
     *                   商户号  |  mch_id  |  是  |  String(32)  |  1230000109  |  微信支付分配的商户号  |
     *                   设备号  |  device_info  |  否  |  String(32)  |  013467007045764  |  终端设备号(门店号或收银设备ID)，默认请传"WEB"  |
     *                   随机字符串  |  nonce_str  |  是  |  String(32)  |  5K8264ILTKCH16CQ2502SI8ZNMTM67VS  |  随机字符串，不长于32位。推荐随机数生成算法  |
     *                   签名  |  sign  |  是  |  String(32)  |  C380BEC2BFD727A4B6845133519F3AD6  |  签名，详见签名生成算法  |
     *                   商品描述  |  body  |  是  |  String(128)  |  Ipad  |  mini 16G 白色  |  商品或支付单简要描述  |
     *                   商品详情  |  detail  |  否  |  String(8192)  |  Ipad  |  mini 16G 白色  |  商品名称明细列表  |
     *                   附加数据  |  attach  |  否  |  String(127)  |  深圳分店  |  附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据  |
     *                   商户订单号  |  out_trade_no  |  是  |  String(32)  |  20150806125346  |  商户系统内部的订单号,32个字符内、可包含字母,  |  其他说明见商户订单号  |
     *                   货币类型  |  fee_type  |  否  |  String(16)  |  CNY  |  符合ISO  |  4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型  |
     *                   总金额  |  total_fee  |  是  |  Int  |  888  |  订单总金额，单位为分，详见支付金额  |
     *                   终端IP  |  spbill_create_ip  |  是  |  String(16)  |  123.12.12.123  |  用户端实际ip  |
     *                   交易起始时间  |  time_start  |  否  |  String(14)  |  20091225091010  |  订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则  |
     *                   交易结束时间  |  time_expire  |  否  |  String(14)  |  20091227091010  |
     *                   订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
     *                   注意：最短失效时间间隔必须大于5分钟  |
     *                   商品标记  |  goods_tag  |  否  |  String(32)  |  WXG  |  商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠  |
     *                   通知地址  |  notify_url  |  是  |  String(256)  |  http://www.weixin.qq.com/wxpay/pay.php  |  接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。  |
     *                   交易类型  |  trade_type  |  是  |  String(16)  |  APP  |  支付类型  |
     *                   指定支付方式  |  limit_pay  |  否  |  String(32)  |  no_credit  |  no_credit--指定不能使用信用卡支付  |
     */
    public boolean pay(Map<String, Object> parameters) {
        boolean result = false;
        try {
            List<NameValuePair> packageParams = getNameValuePairs(parameters);
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring = toXml(packageParams);
            String entity = new String(xmlstring.getBytes(), "ISO8859-1");

            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            Map<String, String> xml = decodeXml(content);
            PayReq payReq = genPayReq(xml);
            result = sendPayReq(payReq);
            Log.i("result", String.valueOf(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @NonNull
    private List<NameValuePair> getNameValuePairs(Map<String, Object> parameters) {
        //必须参数
        final String body = parameters.get("body").toString();
        final String out_trade_no = parameters.get("out_trade_no").toString();
        final String spbill_create_ip = parameters.get("spbill_create_ip").toString();
        final int total_fee = Integer.parseInt(parameters.get("total_fee").toString());
        final String notify_url = parameters.get("notify_url").toString();


        //非必需参数
        final String detail = optValue(parameters, "detail");
        final String attach = optValue(parameters, "attach");
        final String device_info = optValue(parameters, "device_info");
        final String fee_type = optValue(parameters, "fee_type");
        final String time_start = optValue(parameters, "time_start");
        final String time_expire = optValue(parameters, "time_expire");
        final String goods_tag = optValue(parameters, "goods_tag");
        final String limit_pay = optValue(parameters, "limit_pay");

        String nonce_str = genNonceStr();
        List<NameValuePair> packageParams = new LinkedList<>();

        packageParams.add(new BasicNameValuePair("appid", APP_ID));
        addPackageParams(packageParams, "attach", attach);
        packageParams.add(new BasicNameValuePair("body", body));
        addPackageParams(packageParams, "detail", detail);
        addPackageParams(packageParams, "device_info", device_info);
        addPackageParams(packageParams, "fee_type", fee_type);
        addPackageParams(packageParams, "goods_tag", goods_tag);
        addPackageParams(packageParams, "limit_pay", limit_pay);
        packageParams.add(new BasicNameValuePair("mch_id", MCH_ID));
        packageParams.add(new BasicNameValuePair("nonce_str", nonce_str));
        packageParams.add(new BasicNameValuePair("notify_url", notify_url));
        packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));
        packageParams.add(new BasicNameValuePair("spbill_create_ip", spbill_create_ip));
        addPackageParams(packageParams, "time_expire", time_expire);
        addPackageParams(packageParams, "time_start", time_start);
        packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(total_fee)));
        packageParams.add(new BasicNameValuePair("trade_type", "APP"));
        return packageParams;
    }

    public void addPackageParams(List<NameValuePair> packageParams, String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            packageParams.add(new BasicNameValuePair(key, value));
        }
    }


    public String optValue(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key).toString();
        }
        return null;
    }


    private PayReq genPayReq(Map<String, String> resultUnifiedOrder) {
        PayReq req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = MCH_ID;
        req.prepayId = resultUnifiedOrder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        return req;
    }

    private boolean sendPayReq(PayReq req) {
        msgApi.registerApp(APP_ID);
        return msgApi.sendReq(req);
    }


    private static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genPackageSign(List<NameValuePair> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);
        String md5 = MD5.getMessageDigest(sb.toString().getBytes());
        if (TextUtils.isEmpty(md5)) {
            return "";
        }
        return md5.toUpperCase();
    }

    private static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<");
            sb.append(params.get(i).getName());
            sb.append(">");
            sb.append(params.get(i).getValue());
            sb.append("</");
            sb.append(params.get(i).getName());
            sb.append(">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

    private static Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (!"xml".equals(nodeName)) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);
        String md5 = MD5.getMessageDigest(sb.toString().getBytes());
        if (TextUtils.isEmpty(md5)) {
            return "";
        }
        return md5.toUpperCase();
    }
}

