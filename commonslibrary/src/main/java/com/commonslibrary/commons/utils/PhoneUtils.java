package com.commonslibrary.commons.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 电话短信相关工具类
 * 
 * @filename PhoneUtils.java
 * @author WangZhuang
 * @date 2015-5-22 下午4:13:47
 */
public class PhoneUtils {
	public static void call(final Context context, String phone) {
		if (TextUtils.isEmpty(phone)) {
			return;
		} else {
			if (phone.contains("/")) {
				final String[] phoneNum = phone.split("/");
				choosePhoneNumber(context, phoneNum, phone);
			} else {
				final String[] phoneNum = phone.split(" ");
				choosePhoneNumber(context, phoneNum, phone);
			}
		}
	}

	private static void choosePhoneNumber(final Context context, final String[] phoneNum, String phone) {
		if (phoneNum.length > 1) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("联系商家");
			builder.setItems(phoneNum, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String phone = phoneNum[which];
					Intent intent = new Intent();
					// 系统默认的action，用来打开默认的电话界面
					intent.setAction(Intent.ACTION_DIAL);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// 需要拨打的号码
					intent.setData(Uri.parse("tel:" + phone));
					context.startActivity(intent);
				}
			});
			builder.create().show();
		} else {
			Intent intent = new Intent();
			// 系统默认的action，用来打开默认的电话界面
			intent.setAction(Intent.ACTION_DIAL);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 需要拨打的号码
			intent.setData(Uri.parse("tel:" + phone.replaceAll(" ", "")));
			context.startActivity(intent);
			
		}
	}
}
