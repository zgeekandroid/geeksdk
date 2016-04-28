package com.geekandroid.sdk.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geekandroid.sdk.sample.update.NotificationService;
import com.geekandroid.sdk.sample.update.UpdateService;
import com.tbruyelle.rxpermissions.RxPermissions;

import de.greenrobot.event.EventBus;

/**
 * Created by lenovo on 2016/4/25.
 */
public class AppUpdateSampleFragment extends BaseSampleFragment implements View.OnClickListener {
    private static final String TYPE_DIALOG = "type_dialog";
    private static final String TYPE_NOTIFICATION = "type_notification";
    Button showDialog;
    Button showNotification;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public int getResLayoutId() {
        return R.layout.app_update;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showDialog = (Button) view.findViewById(R.id.showDialog);
        showNotification = (Button) view.findViewById(R.id.showNotification);

        showDialog.setOnClickListener(this);
        showNotification.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDialog: {
                toUpdateService(TYPE_DIALOG);
            }
            case R.id.showNotification: {
                toUpdateService(TYPE_NOTIFICATION);
            }
        }
    }

    private void toUpdateService(String type) {
        if (type == TYPE_DIALOG) {
            Intent intent = new Intent(getActivity(), UpdateService.class);
            intent.putExtra("download_type", type);
            getActivity().startService(intent);
        } else {
            Notification noti;
            int smallIcon = getActivity().getApplicationInfo().icon;
            Intent myIntent = new Intent(getActivity(), NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            noti = new NotificationCompat.Builder(getActivity()).setTicker("发现新版本")
                    .setContentTitle("发现新版本").setContentText("aaaaaa").setSmallIcon(smallIcon)
                    .setContentIntent(pendingIntent).build();
            noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noti);
        }

    }
}
