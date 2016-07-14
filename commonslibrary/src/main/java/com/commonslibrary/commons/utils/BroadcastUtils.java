package com.commonslibrary.commons.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadcastUtils {
    public static final String EXIT = "net.loonggg.exit";
    public static final String NEED_UPDATE = "logined.update.data";
    public static final String UPDATE_SERVICE = "update.service.ui";
    /**
     * 定位广播Filter
     */
    public final static String BROADCAST_LOCATION = "ouryue.location";
    public final static String DOWNLOAD = "com.anjoyo.yuexh.update";

    //选择了券后发送广播，改变实际价格
    public final static String COUPON_BROADCAST = "COUPON_BROADCAST";
    //选择了退款后发送广播，改变订单状态
    public final static String REFUND_BROADCAST = "REFUND_BROADCAST";

    //菜单页面滑动改变title状态
    public final static String CHANGE_TITLE = "CHANGE_TITLE";
    //点菜详情菜品的数量改变发广播给消费详情界面改变
    public final static String CHANGE_CONTENT_DETAIL = "CHANGE_CONTENT_DETAIL";
    //点菜详情菜品的数量发生变化
    public final static String CHANGE_CONTENT_NUMBER = "CHANGE_CONTENT_NUMBER";

    /**
     * 刷新客服列表广播
     */
    public final static String CUSTOMERBROADCAST = "CUSTOMERBROADCAST";
    public final static String REFUND_BROADCAST1 = "REFUND_BROADCAST1";


    public static void registerBroadcast(Context context, String broadcastType, BroadcastReceiver broadcastReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(broadcastType);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void sendBrodcast(Context context, String broadcastType, String content) {
        Intent intent = new Intent();
        intent.setAction(broadcastType);
        intent.putExtra("content", content);
        context.sendBroadcast(intent);
    }

    public static void unRegister(Context context, BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        }
    }
}
