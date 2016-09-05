package com.imagerloaderlibrary.imagerloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;

/**
 * date        :  2016-02-03  11:01
 * author      :  Mickaecle gizthon
 * description :
 */
public class ImageLoaderView extends ImageView {

    private DisplayImageOptions.Builder builder;
    private Context context;
    private int roundMargin = 0;
    private int roundCornerRadiusPixels = 10;

    private Integer circleStrokeColor = null;
    private float circleStrokeWidth = 10;

    private int resIdOnFailUri = ImageLoaderManager.getInstance().getResIdOnFailUri();


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

    public Integer getCircleStrokeColor() {
        return circleStrokeColor;
    }

    public void setCircleStrokeColor(Integer circleStrokeColor) {
        this.circleStrokeColor = circleStrokeColor;
    }

    public float getCircleStrokeWidth() {
        return circleStrokeWidth;
    }

    public void setCircleStrokeWidth(float circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
    }

    /**
     * 设置圆角的margin
     *
     * @param roundMargin
     */
    public void setRoundMargin(int roundMargin) {
        this.roundMargin = roundMargin;
    }


    /**
     * 设置圆角的像素，默认30
     *
     * @param roundCornerRadiusPixels
     */
    public void setRoundroundCornerRadiusPixels(int roundCornerRadiusPixels) {
        this.roundCornerRadiusPixels = roundCornerRadiusPixels;
    }


    public DisplayImageOptions.Builder getBuilder() {
        return builder;
    }

    public void init(Context context) {
        this.context = context;

        builder = new DisplayImageOptions.Builder().//

                cacheInMemory(true).// 内存缓存
                cacheOnDisk(true).// 文件缓存
                considerExifParams(true)//
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        ;
    }
    private Bitmap getScaleBitmap(ImageSize imageSize, int resIdOnFailUri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resIdOnFailUri, options);
        if (imageSize == null) {
            options.inSampleSize = 1;
        } else {
            options.inSampleSize = ImageSizeUtils.computeImageSampleSize(new ImageSize(options.outWidth, options.outHeight), imageSize, ViewScaleType.FIT_INSIDE, false);
        }
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(), resIdOnFailUri, options);
    }
    public void setUrl(String url) {
       setUrl(url,null,false);
    }

    public void setUrl(String url, final ImageSize imageSize, final boolean isGray) {
        ImageViewAware viewAware = new ImageViewAware(this);
        final DisplayImageOptions options = builder.showImageOnLoading(resIdOnFailUri).// 图片加载时显示的图片
                showImageForEmptyUri(resIdOnFailUri).// Uri为空显示图片
                showImageOnFail(resIdOnFailUri).// 图片加载失败显示图片
                build();
        ImageLoader.getInstance().displayImage(url, viewAware, options, imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (isGray) {
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0);
                    ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);

                    getDrawable().setColorFilter(colorMatrixColorFilter);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageBitmap(bitmap);
            }
        }, null);

    }

    public void setCircleUrl(String url) {
        setCircleUrl(url,null);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     */
    public void setCircleUrl(String url, final ImageSize imageSize) {
        ImageViewAware viewAware = new ImageViewAware(this);
        final DisplayImageOptions options = builder.showImageOnLoading(resIdOnFailUri).// 图片加载时显示的图片
                showImageForEmptyUri(resIdOnFailUri).// Uri为空显示图片
                showImageOnFail(resIdOnFailUri).// 图片加载失败显示图片
                build();
        ImageLoader.getInstance().displayImage(url, viewAware, options, imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, circleStrokeColor, circleStrokeWidth));
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, circleStrokeColor, circleStrokeWidth));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, circleStrokeColor, circleStrokeWidth));
            }
        }, null);
    }

    public void setRoundUrl(String url) {
        setRoundUrl(url,null);
    }
    /**
     * 加载圆角
     *
     * @param url
     */
    public void setRoundUrl(String url, final ImageSize imageSize) {
        ImageViewAware viewAware = new ImageViewAware(this);
        final DisplayImageOptions options = builder.showImageOnLoading(resIdOnFailUri).// 图片加载时显示的图片
                showImageForEmptyUri(resIdOnFailUri).// Uri为空显示图片
                showImageOnFail(resIdOnFailUri).// 图片加载失败显示图片
                build();
        ImageLoader.getInstance().displayImage(url, viewAware, options, imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(bitmap, roundCornerRadiusPixels, roundMargin));
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(bitmap, roundCornerRadiusPixels, roundMargin));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Bitmap bitmap = getScaleBitmap(imageSize, resIdOnFailUri);
                setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(bitmap, roundCornerRadiusPixels, roundMargin));
            }
        }, null);
    }

    public void setUrl(String uri, DisplayImageOptions options, ImageSize targetSize, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(this), options, targetSize, listener, progressListener);
    }


}
