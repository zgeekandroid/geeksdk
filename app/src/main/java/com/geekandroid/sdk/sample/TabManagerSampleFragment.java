package com.geekandroid.sdk.sample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.commonslibrary.commons.utils.LogUtils;
import com.geekandroid.sdk.uiframework.TabManager;

/**
 * date        :  2016-04-20  13:39
 * author      :  Mickaecle gizthon
 * description :
 */
public class TabManagerSampleFragment extends BaseSampleFragment {
    private TabManager manager;

    @Override
    public int getResLayoutId() {
        return R.layout.tab_manager;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (manager == null){
            manager = new TabManager.Build().build(this, view);
//            manager = new TabManager.Build().findViewById(android.R.id.tabhost).setUp(com.geekandroid.sdk.R.id.tab_content).build(this, view);
            manager.addTab(CommonSampleFragment.class,"common",getItemIndicator("common",R.mipmap.ic_launcher));
            manager.addTab(LocationSampleFragment.class,"location",getItemIndicator("location",R.mipmap.ic_launcher));
            manager.addTab(ImageloaderFragment.class,"image",getItemIndicator("image",R.mipmap.ic_launcher));

            manager.getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    LogUtils.i(tabId);
                }
            });
        }

        return view;
    }
    LayoutInflater inflater ;

    public View getItemIndicator(String text,int resId){
        View view = null;
        if (inflater == null){
            inflater = LayoutInflater.from(getContext());
        }
         view =  inflater.inflate(R.layout.item_main_tab,null);

        TextView textView = (TextView) view.findViewById(R.id.item_main_tab_view);
        textView.setText(text);

        Drawable drawable = ContextCompat.getDrawable(getContext(),resId);
        drawable.setBounds(0, 0, 60, 60);
        textView.setCompoundDrawablePadding(10);
        textView.setCompoundDrawables(null, drawable, null, null);

        return view;
    }

}