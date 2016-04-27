package com.geekandroid.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.geekandroid.common.config.SystemConfig;
import com.geekandroid.sdk.sample.domain.VersionEvent;
import com.geekandroid.sdk.sample.update.domain.DownloadInfo;
import com.geekandroid.sdk.sample.update.domain.VersionBean;
import com.geekandroid.sdk.sample.update.views.CustomDialog;
import com.geekandroid.sdk.sample.utils.DialogUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * date        :  2016-04-20  13:46
 * author      :  Mickaecle gizthon
 * description :
 */
public class ContentActivity extends AppCompatActivity {
    public static Fragment showFragment = null;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        EventBus.getDefault().register(this);
        if (showFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, showFragment).commit();
        }
    }

    Fragment current;

    public void switchContent(Fragment from, Fragment to) {
        if (current != to) {
            current = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()/*.setCustomAnimations(
                    android.R.anim.fade_in, R.anim.fade_out)*/;
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe
    public void onEventMainThread(VersionEvent event) {
        VersionBean versionBean = event.getVersionBean();
        String dialog_certain = "马上下载";
        String dialog_cancel = "下次提醒";
        String title = "有新版本：";
        if (null != versionBean) {
            //String description = versionBean.getBackinfo().getApp_update_description();
            String description = "快点更新把";
            DialogUtils.showUpdateDialog(ContentActivity.this, title, dialog_certain, dialog_cancel, description);
        }

        int progress = event.getProgress();
        int contentLength = (int) event.getContentLength();
        if (progress > 0) {
            pd.setMax(contentLength);
            pd.setProgress(progress);
        }

    }

    public void setDownLoadBtn() {
        DownloadInfo downloadInfo = new DownloadInfo("http://www.maicaim.com//App/PurchaseAPP.apk", "maicaime" + "2" + ".apk", SystemConfig.getSystemFileDir(), true);
        //callSerViceDownload();
        VersionEvent versionEvent = new VersionEvent();
        versionEvent.setDownloadInfo(downloadInfo);
        EventBus.getDefault().post(versionEvent);
        pd = DialogUtils.showDownLoadDialog(ContentActivity.this);
    }
}
