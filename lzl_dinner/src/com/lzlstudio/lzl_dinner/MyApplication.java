package com.lzlstudio.lzl_dinner;

import com.lzlstudio.lzl_dinner.srv.DataUpdateService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

public class MyApplication extends Application {

	public static MyApplication INSTANCE;
	@Override
	public void onCreate()
	{
		super.onCreate();
		INSTANCE = this;
		// Start resource prepare service
		startService(new Intent(this, DataUpdateService.class));
		// Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        															  //.taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        															  .threadPoolSize(8)
        															  .build();
        ImageLoader.getInstance().init(config);
	}
}
