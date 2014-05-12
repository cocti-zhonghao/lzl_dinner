package com.lzlstudio.lzl_dinner;

import android.app.Application;

public class MyApplication extends Application {

	public static MyApplication INSTANCE;
	@Override
	public void onCreate()
	{
		super.onCreate();
		//
		INSTANCE = this;
		//
		MenuLoaderHelper.getInstance().Load(this);
	}
}
