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

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.commonslibrary.commons.handler.WeakHandlerNew;
import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.LogUtils;
import com.geekandroid.sdk.pay.IPay;
import com.geekandroid.sdk.pay.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;


public abstract class CHAlipay extends IPay {

    private WeakHandlerNew handlerNew = new WeakHandlerNew(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            handleRealMessage(msg);
            return false;
        }
    });
    private String private_key = "";

    protected void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }


    @Override
    public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {

    }

    @Override
    public void getPayResult(Map<String, Object> params, RequestCallBack<String> callBack) {

    }

    /**
     * Handler 中支付成功的调用
     *
     * @param result
     */
    public abstract void callClientSuccess(PayResult result);

    /**
     * 支付返回回调
     */


    protected void handleRealMessage(Message msg) {
        switch (msg.what) {
            case ALI_SDK_PAY_FLAG: {
                String str = (String) msg.obj;
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultInfo = payResult.getResult();
                String resultStatus = payResult.getResultStatus();

                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 支付宝支付成功后向后台询问是否支付支付成功

                    callClientSuccess(payResult);

                } else if (TextUtils.equals(resultStatus, "8000")) {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    hideProgress("错误:" + resultStatus + ",支付结果确认中");
                } else if (TextUtils.equals(resultStatus, "6001")) {
                    hideProgress("错误:" + resultStatus + ",已取消支付");

                } else if (TextUtils.equals(resultStatus, "6002")) {
                    hideProgress("错误:" + resultStatus + ",网络连接出错，请重试");
                } else if (TextUtils.equals(resultStatus, "4000")) {
                    hideProgress("错误:" + resultStatus + ",系统繁忙，请稍后尝试");
                }
                break;
            }
            case ALI_SDK_CHECK_FLAG: {
                hideProgress();
                //ToastUtils.show(context, "检查结果为：" + msg.obj);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 需要签名的参数
     *
     * @param resultInfo
     */
    public String getSignContent(String resultInfo) {
        // 所有返回的参数
        int removeIndex = resultInfo.indexOf("&sign_type");
        String content = resultInfo.substring(0, removeIndex);
        return content;
    }

    /**
     * 得到原来的sign签名
     */
    protected String getSign(String resultInfo) {
        String sign = "";
        String[] params = resultInfo.split("&");
        for (int i = 0; i < params.length; i++) {
            String string = params[i];
            boolean startsWith = params[i].startsWith("sign=");
            if (startsWith) {
                sign = params[i];
                sign = sign.replace("sign=", "").replace("\"", "");
            }
        }
        return sign;
    }


    protected void doRealPay(Map<String, Object> param) {

        if (TextUtils.isEmpty(private_key)) {
            LogUtils.e("必须指定支付宝的private_key");
            return;
        }

        if (isEmpty(param.get("partner")) ||
                isEmpty(param.get("seller_id")) ||
                isEmpty(param.get("out_trade_no")) ||
                isEmpty(param.get("total_fee")) ||
                isEmpty(param.get("subject")) ||
                isEmpty(param.get("body")) ||
                isEmpty(param.get("notify_url"))
                ) {
            LogUtils.e("必须指定参数：[partner,seller_id,out_trade_no,total_fee,notify_url,subject,body]");
            return;
        }

        String partner = param.get("partner").toString();
        String seller_id = param.get("seller_id").toString();
        String out_trade_no = param.get("out_trade_no").toString();
        String total_fee = String.valueOf(param.get("total_fee"));
        String notify_url = param.get("notify_url").toString();
        String subject = param.get("subject").toString();
        String body = param.get("body").toString();
        // 订单
        String orderInfo = getOrderInfo(partner, seller_id, out_trade_no, total_fee, notify_url, subject, body);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = ALI_SDK_PAY_FLAG;
                msg.obj = result;
                handlerNew.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 创建订单信息
     */

    public String getOrderInfo(String partner, String seller_id, String out_trade_no, String total_fee, String notify_url, String subject, String body) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + seller_id + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        orderInfo += "&total_fee=" + "\"" + total_fee + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    public String sign(String content) {
        return SignUtils.sign(content, private_key);
    }

    /**
     * get the sign type we use. 获取签名方式
     */

    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
