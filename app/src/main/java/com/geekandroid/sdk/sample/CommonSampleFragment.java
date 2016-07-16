package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.commonslibrary.commons.net.BaseRemoteModel;
import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Hashtable;
import java.util.Map;

/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class CommonSampleFragment extends BaseSampleFragment {
    private static final String TAG = "CommonSampleFragment";

    private Button button;
    private BaseRemoteModel model;
    private RxPermissions rxPermissions;

    @Override
    public int getResLayoutId() {
        return R.layout.common;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        rxPermissions = RxPermissions.getInstance(getActivity());
        rxPermissions.setLogging(true);
        button = (Button) view.findViewById(R.id.doGetNetData);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPost();
            }
        });
        return view;
    }

    private void init() {
        model = new BaseRemoteModel();
    }

    private void doPost(){
        String url = "http://data.maicaim.com/Purchase/User/userInfo.ashx";
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("port_password", "021e7d14f5f93ada5c64de8807760e68");
        parameters.put("user_id","TUser8e466d7145084cc1859a3dfe8c13d317");
        parameters.put("h_userid", "TUser8e466d7145084cc1859a3dfe8c13d317");
        model.doPost(url, parameters, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("-------------"+result);
                ToastUtils.show(getActivity(),result);
            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                ToastUtils.show(getActivity(),errorMessage);
            }
        });
    }

}