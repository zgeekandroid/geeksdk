package com.geekandroid.sdk.imageloader;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * date        :  2016-04-29  14:33
 * author      :  Mickaecle gizthon
 * description :
 */
public class RountBitmapDisplayer implements BitmapDisplayer {


    public RountBitmapDisplayer() {
     super();
    }


    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new RoundImageDrawable(bitmap));
    }


}