package com.imagerloaderlibrary.imagerloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.commonslibrary.commons.utils.DeviceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
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
    private int roundMargin = 0;
    private int roundCornerRadiusPixels = 10;

    private Integer circleStrokeColor = null;
    private float circleStrokeWidth = 0;

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
//                cacheOnDisk(true).// 文件缓存
        considerExifParams(true).//
                imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
    }

    public void setUrl(String url) {
        ImageLoader.getInstance().displayImage(url, this, builder.showImageOnLoading(resIdOnFailUri).// 图片加载时显示的图片
                showImageForEmptyUri(resIdOnFailUri).// Uri为空显示图片
                showImageOnFail(resIdOnFailUri).// 图片加载失败显示图片.
                build());

    }

    /**
     * 加载圆形图片
     *
     * @param url
     */
    public void setCircleUrl(String url) {

        ImageLoader.getInstance().displayImage(url, this, builder.displayer(new CircleBitmapDisplayer(circleStrokeColor,circleStrokeWidth))
                .build(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIdOnFailUri);
                setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, circleStrokeColor,circleStrokeWidth));
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIdOnFailUri);
                setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, circleStrokeColor,circleStrokeWidth));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIdOnFailUri);
                setImageDrawable(new CircleBitmapDisplayer.CircleDrawable(bitmap, circleStrokeColor,circleStrokeWidth));
            }
        });
    }

    /**
     * 加载圆角
     *
     * @param url
     */
    public void setRoundUrl(String url) {
        ImageLoader.getInstance().displayImage(url, this, builder.displayer(new RoundedBitmapDisplayer(roundCornerRadiusPixels, roundMargin))
                .build(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIdOnFailUri);
                setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(bitmap, roundCornerRadiusPixels, roundMargin));
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIdOnFailUri);
                setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(bitmap, roundCornerRadiusPixels, roundMargin));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIdOnFailUri);
                setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(bitmap, roundCornerRadiusPixels, roundMargin));
            }
        });
    }

    public void setUrl(String uri, DisplayImageOptions options, ImageSize targetSize, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(this), options, targetSize, listener, progressListener);
    }

    /**
     * 根据屏幕尺寸比例设置是否 变灰
     *
     * @param url
     * @param weight
     * @param isGray
     */
    public void setUrl(String url, final double weight, final boolean isGray) {
        ImageViewAware viewAware = new ImageViewAware(this);
        ImageSize imageSize = null;
        if (weight <= 0) {
        } else {
            final int width = DeviceUtils.getScreenWidth(context);
            imageSize = new ImageSize(width, (int) (width / weight));
        }
        ImageLoader.getInstance().displayImage(url, viewAware, builder.showImageOnLoading(resIdOnFailUri).// 图片加载时显示的图片
                showImageForEmptyUri(resIdOnFailUri).// Uri为空显示图片
                showImageOnFail(resIdOnFailUri).// 图片加载失败显示图片
                build(), imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                setImageBitmap(loadedImage);

                if (isGray) {
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

    /**
     * 设置当前imageview 和 父view 自动适配大小
     *
     * @param url
     * @param parentView
     */
    public void setUrl(String url, final double weight, final View parentView) {
        final int width = DeviceUtils.getScreenWidth(context);
        ImageViewAware viewAware = new ImageViewAware(this);
        ImageSize imageSize = null;
        if (weight <= 0) {
        } else {
            imageSize = new ImageSize(width, (int) (width / weight));
        }
        ImageLoader.getInstance().displayImage(url, viewAware, builder.showImageOnLoading(resIdOnFailUri).// 图片加载时显示的图片
                showImageForEmptyUri(resIdOnFailUri).// Uri为空显示图片
                showImageOnFail(resIdOnFailUri).// 图片加载失败显示图片
                build(), imageSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                ViewGroup.LayoutParams params = parentView.getLayoutParams();
                params.height = (int) (width / weight);
                params.width = width;
                parentView.setLayoutParams(params);

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

    /**
     * 设置当前imageview 和 父view 自动适配大小，默认 2.185 的比例
     *
     * @param url
     * @param parentView
     */
    public void setUrl(String url, final View parentView) {
        double weight = 2.185;
        setUrl(url, weight, parentView);
    }


}
