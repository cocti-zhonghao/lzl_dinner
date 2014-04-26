package com.lzlstudio.lzl_dinner;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate()
	{
		super.onCreate();
		MenuLoaderHelper.getInstance().Load(this);
	}
}
