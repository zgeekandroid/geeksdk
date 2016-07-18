/*
package com.geekandroid.sdk.sample.aliandwxpay;

import android.content.Intent;
import android.os.Bundle;

import com.commonslibrary.commons.utils.ToastUtils;
import com.geekandroid.sdk.base.BaseActivity;
import com.geekandroid.sdk.sample.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

*/
/**
 * date        :  2016-04-02  18:09
 * author      :  Mickaecle gizthon
 * description :
 *//*

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logI("微信回调");
        setContentView(R.layout.activity_pay_result);
        api = WXAPIFactory.createWXAPI(this, WeiXinPay.APP_ID, false);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            success();
            switch (resp.errCode) {
                case 0:
                    // 支付成功后，询问后台是否支付成功
//                    LocalCacheUtils.getInstance().updateUserInfo(new IMyAccountView() {
//                        @Override
//                        public void onQueryUserInfoSucess(UserInfo.BackinfoEntity backinfoEntity) {
//                            success();
//                        }
//
//                        @Override
//                        public void onQueryUserInfoFail(String err) {
//
//                        }
//                    });

                    break;
                case -1:
                    ToastUtils.show(this, "错误，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。");
                    break;
                case -2:
                    ToastUtils.show(this, "取消支付");
                    break;
                default:
                    break;
            }

        }
    }


    private void success() {
        //跳转activity
        finish();
//        if(AppContext.mRechargeActivity!=null){
//            AppContext.mRechargeActivity.finish(); 
//        }
//        if(AppContext.mOrderDetailAcitvity!=null){
//            AppContext.mOrderDetailAcitvity.finish(); 
//        }
    }


}
*/
