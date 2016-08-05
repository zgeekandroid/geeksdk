package com.geekandroid.sdk.sample;

import android.app.Application;
import android.content.Context;

import com.commonslibrary.commons.config.SystemConfig;
import com.geekandroid.sdk.jpushlibrary.push.impl.JPushImpl;
import com.geekandroid.sdk.maplibrary.impl.BDLocationImpl;
import com.imagerloaderlibrary.imagerloader.ImageLoaderManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class SampleApplication extends Application {

	public DisplayImageOptions options;

	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader(getApplicationContext());
		// 初始化ImageLoader
		ImageLoaderManager.getInstance().init(this);
		SystemConfig.setDebug(true);
		JPushImpl.getInstance().setDebugMode(SystemConfig.isDebug());
		JPushImpl.getInstance().init(this);
		BDLocationImpl.getInstance().init(getApplicationContext());
	}

	public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
				.memoryCacheSize(50 * 1024 * 1024) //设置内存缓存的大小
				.diskCacheFileCount(200)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
