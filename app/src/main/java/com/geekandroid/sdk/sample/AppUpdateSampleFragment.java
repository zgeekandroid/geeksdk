package com.geekandroid.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geekandroid.sdk.sample.update.UpdateService;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Created by lenovo on 2016/4/25.
 */
public class AppUpdateSampleFragment extends BaseSampleFragment implements View.OnClickListener {
    Button showDialog;
    Button showNotification;

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
                toUpdateService();
            }
            case R.id.showNotification: {
                toUpdateService();
            }
        }
    }

    private void toUpdateService() {
        Intent intent = new Intent(getActivity(), UpdateService.class);
        getActivity().startService(intent);
    }
}
