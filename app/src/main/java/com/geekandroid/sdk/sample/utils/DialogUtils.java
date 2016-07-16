package com.geekandroid.sdk.sample.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonslibrary.commons.utils.DeviceUtils;
import com.commonslibrary.commons.utils.ToastUtils;
import com.geekandroid.sdk.sample.R;
import com.geekandroid.sdk.update.onDialogBtnListerner;
import com.geekandroid.sdk.update.views.AutoWebView;
import com.geekandroid.sdk.update.views.CustomDialog;

/**
 * Created by lenovo on 2016/4/27.
 */
public class DialogUtils {
    static ProgressDialog pd;

    public static void showUpdateDialog(Activity activity, String tiele, String dialog_certain, String dialog_cancel, String description,onDialogBtnListerner mOnDialogBtnListerner) {
        final CustomDialog builder = new CustomDialog(activity, R.style.dialog);
        View versionDialogView = View.inflate(activity, R.layout.dialog_version_info, null);
        TextView bt_cancel_version = (TextView) versionDialogView.findViewById(R.id.bt_cancel_version);
        TextView tv_sure_version = (TextView) versionDialogView.findViewById(R.id.tv_sure_version);
        TextView dialog_version_title = (TextView) versionDialogView.findViewById(R.id.dialog_version_title);
        AutoWebView webView = (AutoWebView) versionDialogView.findViewById(R.id.web_view);
        tv_sure_version.setText(dialog_certain);
        bt_cancel_version.setText(dialog_cancel);
        dialog_version_title.setText(tiele);
        webView.loadHtml(description);
        setWebViewHeight(webView, activity);
        bt_cancel_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                mOnDialogBtnListerner.cancelClick();
            }
        });
        tv_sure_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogBtnListerner.sureClick();
                builder.dismiss();
            }
        });
        setDialogWH(builder, versionDialogView, activity);
    }

    public static ProgressDialog showDownLoadDialog(Activity activity) {
        String netSate = DeviceUtils.getNetworkType(activity);
        pd = new ProgressDialog(activity);
        if ("WIFI".equals(netSate)) {
            //WIFI环境,提示用户后台下载，通知Service进行下载
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("正在下载，请稍候...");
            pd.setButton("点击隐藏进度对话框", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ToastUtils.show(activity, "程序在后台下载，下载完成后请安装");
                }
            });
            pd.show();
            return pd;
        } else if ("MOBILE-4G".equals(netSate) || "MOBILE-2G".equals(netSate) || "MOBILE-3G".equals(netSate)) {
            //询问用户是否4G下载
            //show4GRemindDialog();
            return null;
        } else {
            //网络环境不好
            ToastUtils.show(activity, "您的网络状况不佳，取消下载");
            return null;
        }
    }


    private static void setWebViewHeight(AutoWebView webView, Activity activity) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) webView.getLayoutParams();
        linearParams.height = (int) (DeviceUtils.getScreenHeight(activity) * 0.3);
        webView.setLayoutParams(linearParams);
    }

    private static void setDialogWH(CustomDialog builder, View versionDialogView, Activity activity) {
        builder.setContentView(versionDialogView);
        WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
        lp.width = (int) (DeviceUtils.getScreenWidth(activity) * 0.8); //设置宽度
        builder.getWindow().setAttributes(lp);
        builder.show();
    }


    public static void showInstallDialog(Activity activity, String filePath) {
        final CustomDialog builder = new CustomDialog(activity, R.style.dialog);
        View versionDialogView = View.inflate(activity, R.layout.dialog_install_info, null);
        TextView bt_cancel_version = (TextView) versionDialogView.findViewById(R.id.bt_cancel_version);
        TextView tv_sure_version = (TextView) versionDialogView.findViewById(R.id.tv_sure_version);
        TextView dialog_version_title = (TextView) versionDialogView.findViewById(R.id.dialog_version_title);
        TextView dialog_version_content = (TextView) versionDialogView.findViewById(R.id.dialog_version_content);
        dialog_version_content.setVisibility(View.GONE);
        builder.setContentView(versionDialogView);
        tv_sure_version.setText("立即安装");
        bt_cancel_version.setText("稍后安装");
        dialog_version_title.setText("新版本已下载完成，请问是否现在安装?");
        bt_cancel_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        tv_sure_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //安装
                DeviceUtils.install(activity, filePath);
                builder.dismiss();
            }
        });
        setDialogWH(builder, versionDialogView, activity);
        builder.show();
    }

}
