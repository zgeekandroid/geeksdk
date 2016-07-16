package com.geekandroid.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagerloaderlibrary.imagerloader.ImageLoaderView;

/**
 *
 *@author:BingCheng
 *@date:2016/4/28 17:01
 */
public class ImageloaderFragment extends BaseSampleFragment {
    private static final String TAG = "PaySampleFragment";


    @Override
    public int getResLayoutId() {
        return R.layout.fragment_sample_imager_loader;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImageLoaderView normal = (ImageLoaderView) view.findViewById(R.id.normal);
        ImageLoaderView circle = (ImageLoaderView) view.findViewById(R.id.circle);
        ImageLoaderView round = (ImageLoaderView) view.findViewById(R.id.round);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        String url = "http://img4q.duitang.com/uploads/item/201411/04/20141104225919_ZR3h5.thumb.224_0XX.jpeg";
        normal.setUrl(url);
        circle.setCircleUrl(url);
        round.setRoundUrl(url);
        

        
        return view;
    }
   

}
