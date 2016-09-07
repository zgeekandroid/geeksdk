package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonslibrary.commons.net.BaseRemoteModel;
import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.ToastUtils;
import com.commonslibrary.commons.net.BaseRxJavaRemoteModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Hashtable;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class CommonSampleFragment extends BaseSampleFragment {
    private static final String TAG = "CommonSampleFragment";

    private BaseRemoteModel model = new BaseRemoteModel();
    private RxPermissions rxPermissions;
    BaseRxJavaRemoteModel rxJavaRemoteModel = new BaseRxJavaRemoteModel();

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
        view.findViewById(R.id.doGetNetData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPost();
            }
        });
        view.findViewById(R.id.doGetNetRxData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRxPost();
            }
        });
        return view;
    }


    private void doPost() {
        String url = "http://data.maicaim.com/Purchase/User/userInfo.ashx";
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("port_password", "021e7d14f5f93ada5c64de8807760e68");
        parameters.put("user_id", "TUser8e466d7145084cc1859a3dfe8c13d317");
        parameters.put("h_userid", "TUser8e466d7145084cc1859a3dfe8c13d317");
//        model.setMainThread(false);
        model.doPost(url, parameters, new RequestCallBack<ResultBean>() {
            @Override
            public void onSuccess(ResultBean result) {
                LogUtils.i("-------------" + result);
                ToastUtils.show(getActivity(), result.toString());
//                Observable<ResultBean> observable = Observers.create(new Su)
            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                ToastUtils.show(getActivity(), errorMessage);
            }
        });

    }

    private void doRxPost() {
        String url = "http://data.maicaim.com/Purchase/User/userInfo.ashx";
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("port_password", "021e7d14f5f93ada5c64de8807760e68");
        parameters.put("user_id", "TUser8e466d7145084cc1859a3dfe8c13d317");
        parameters.put("h_userid", "TUser8e466d7145084cc1859a3dfe8c13d317");
        rxJavaRemoteModel.doRxPost(url, parameters, ResultBean.class).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.i("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i("" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResultBean message) {
                        LogUtils.i(message);
                        ToastUtils.show(getContext(), message.toString());
                    }
                });


    }

}