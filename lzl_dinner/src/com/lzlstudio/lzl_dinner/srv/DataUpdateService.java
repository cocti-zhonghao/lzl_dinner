package com.lzlstudio.lzl_dinner.srv;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataUpdateService extends Service {

	@Override
	public void onCreate()
	{}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//startBackgroundTask(intent, startId);
		if(null == intent)
		{
			//killed then restarted by run time
		}
		return Service.START_STICKY;
	}
}
