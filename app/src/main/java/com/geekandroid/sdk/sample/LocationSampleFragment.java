package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.geekandroid.sdk.sample.commonslibrary.net.RequestCallBack;
import com.geekandroid.sdk.sample.commonslibrary.utils.ToastUtils;
import com.geekandroid.sdk.sample.maplibrary.Location;
import com.geekandroid.sdk.sample.maplibrary.impl.BDLocationImpl;


/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class LocationSampleFragment extends BaseSampleFragment {
    private Button button;
    private Button nacigation;
    @Override
    public int getResLayoutId() {
        return R.layout.location;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        button = (Button) view.findViewById(R.id.location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
            }
        });



        return view;
    }


    private void location() {
        BDLocationImpl.getInstance().start(new RequestCallBack<Location>() {
            @Override
            public void onSuccess(Location location) {
                ToastUtils.show(getActivity(),location.toString());
                BDLocationImpl.getInstance().stop();

            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
               ToastUtils.show(getActivity(),errorMessage);
                BDLocationImpl.getInstance().stop();
            }
        });
    }





}