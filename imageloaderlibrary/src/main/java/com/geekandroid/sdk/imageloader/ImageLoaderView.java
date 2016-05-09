package com.geekandroid.sdk.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geekandroid.imageloader.R;
import com.geekandroid.sdk.commons.utils.DeviceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * date        :  2016-02-03  11:01
 * author      :  Mickaecle gizthon
 * description :
 */
public class ImageLoaderView extends ImageView {

    private DisplayImageOptions.Builder builder;
    private Context context;

    public ImageLoaderView(Context context) {
        super(context);
        init(context);
    }

    public ImageLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        builder = new DisplayImageOptions.Builder().//
                showImageOnLoading(R.drawable.default_image).// 图片加载时显示的图片
                showImageForEmptyUri(R.drawable.default_image).// Uri为空显示图片
                showImageOnFail(R.drawable.default_image).// 图片加载失败显示图片
                cacheInMemory(true).// 内存缓存
//                cacheOnDisk(true).// 文件缓存
                considerExifParams(true).//
                imageScaleType(ImageScaleType.EXACTLY_STRETCHED).
                displayer(new RoundedBitmapDisplayer(5));
    }

    public void setUrl(String url) {
        ImageLoader.getInstance().displayImage(url, this, builder.build());
    }

    public void setCircleUrl(String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                 .cacheInMemory(true)
//                .cacheOnDisk(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
        ImageLoader.getInstance().displayImage(url, this, options);
    }
    public void setRoundUrl(String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                 .cacheInMemory(true)
//                .cacheOnDisk(true)
                .displayer(new RountBitmapDisplayer())
                .build();
        ImageLoader.getInstance().displayImage(url, this, options);
    }


    public void setUrl(String url, final double weight, final boolean saleOut) {

        ImageViewAware viewAware = new ImageViewAware(this);
        final int width = DeviceUtils.getScreenWidth(context);
        ImageSize imageSize = new ImageSize(width, (int) (width / weight));

        ImageLoader.getInstance().displayImage(url, viewAware, builder.build(), imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                setImageBitmap(loadedImage);

                if (saleOut) {
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0);
                    ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);

                    getDrawable().setColorFilter(colorMatrixColorFilter);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });

    }


    public void setUrl(String url, final View parente) {
        double weight = 2.185;

        ImageViewAware viewAware = new ImageViewAware(this);
        final int width = DeviceUtils.getScreenWidth(context);
        ImageSize imageSize = new ImageSize(width, (int) (width / weight));

        ImageLoader.getInstance().displayImage(url, viewAware, builder.build(), imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                ViewGroup.LayoutParams params = parente.getLayoutParams();
                params.height = (int) (width / 2.185);
                params.width = width;

                parente.setLayoutParams(params);

                setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });

    }


}
