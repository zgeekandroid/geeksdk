package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonslibrary.commons.net.BaseRemoteModel;
import com.commonslibrary.commons.net.BaseRxJavaRemoteModel;
import com.commonslibrary.commons.net.RequestCallBack;
import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        LogUtils.isfomat = false;
        model.doPost(url, parameters, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                generateEntityFile(result);
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

    private void generateEntityFile(String result) {
        boolean isCover = true;
        String targetDir = ".";
        String targetFileName = "ResultEntity";
        String pkgName = getClass().getPackage().getName();


        File desDir = new File(targetDir);
        if (!desDir.exists()) {
            boolean success = desDir.mkdirs();
            String log = "创建文件夹 " + desDir.getName() + (success ? "成功" : "失败");
            LogUtils.i(log);
        }

        File desFile = new File(targetDir,targetFileName +".java");

        if (!desFile.exists()){

            if (!desFile.canWrite()){
                LogUtils.e("没有权限创建"+desFile.getName()+"文件,失败,返回");
                return;
            }

            try {
               boolean success =  desFile.createNewFile();
                String log = "创建文件 " + desFile.getName() + (success ? "成功" : "失败");
                LogUtils.i(log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            FileWriter filerWriter = new FileWriter(desFile, isCover);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);

            String pkgline = "package " + pkgName + ";";
            bufWriter.write(pkgline);
            bufWriter.newLine();

            String prefix ="/**";
            bufWriter.write(prefix);
            bufWriter.newLine();

            String json =  LogUtils.prettyJson(result);
            bufWriter.write(json);
            bufWriter.newLine();

            String subfix ="*/";
            bufWriter.write(subfix);
            bufWriter.newLine();

            String clsline = "public class " + targetFileName + "{";
            bufWriter.write(clsline);
            bufWriter.newLine();
            bufWriter.newLine();

            bufWriter.write("}");
            bufWriter.newLine();

            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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