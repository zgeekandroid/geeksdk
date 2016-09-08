package com.imagerloaderlibrary.imagerloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.commonslibrary.commons.config.SystemConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;

/**
 * date        :  2016-02-03  11:16
 * author      :  Mickaecle gizthon
 * description :
 */
public class ImageLoaderManager {
    static ImageLoaderManager INSTANCE;
    private int resIdOnFailUri = R.drawable.default_image;
    private ImageLoaderManager() {

    }

    public static ImageLoaderManager getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ImageLoaderManager();
        }
        return INSTANCE;
    }

    public int getResIdOnFailUri() {
        return resIdOnFailUri;
    }

    public void setResIdOnFailUri(int resIdOnFailUri) {
        this.resIdOnFailUri = resIdOnFailUri;
    }

    public void init(Context context) {

        File cachefile = new File(SystemConfig.getSystemCacheDir());

        if (!cachefile.canWrite()) {
            cachefile = context.getCacheDir();
        }

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context).//
                threadPriority(Thread.NORM_PRIORITY - 2).//
                denyCacheImageMultipleSizesInMemory().//
                diskCacheFileNameGenerator(new Md5FileNameGenerator()).//
                tasksProcessingOrder(QueueProcessingType.LIFO) //
//                .memoryCacheExtraOptions(600, 200)//
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                       . diskCache(new UnlimitedDiskCache(cachefile)) // default
                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(100)
//diskCacheExtraOptions(600, 200, null)

                ;

        if (SystemConfig.isDebug()) {
            builder.writeDebugLogs();// 打印日志
        }

        ImageLoaderConfiguration config = builder.build();// 创建
        // 初始化,只能调用一次
        ImageLoader.getInstance().init(config);

    }




    public void clearImageLoaderCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void setPauseOnScroll(AbsListView view) {
        view.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true, null));
    }

    public void setPauseOnScroll(RecyclerView view) {
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {


                switch (scrollState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        ImageLoader.getInstance().resume();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        ImageLoader.getInstance().pause();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        ImageLoader.getInstance().pause();
                        break;
                }


                super.onScrollStateChanged(recyclerView, scrollState);
            }
        });
    }


}
