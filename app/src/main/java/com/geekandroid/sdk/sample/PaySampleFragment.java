package com.geekandroid.sdk.sample;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 *@author:BingCheng
 *@date:2016/4/28 17:01
 */
public class PaySampleFragment extends BaseSampleFragment {
    private static final String TAG = "PaySampleFragment";


    @Override
    public int getResLayoutId() {
        return R.layout.fragment_sample_pay;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        TextView tv1 = (TextView) view.findViewById(R.id.tv1);
        return view;
    }
   

}
