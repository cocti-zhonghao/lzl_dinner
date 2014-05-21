package com.lzlstudio.lzl_dinner.srv;

import com.lzlstudio.lzl_dinner.MenuLoaderHelper;
import com.lzlstudio.lzl_dinner.dao.MenuCategoryDao;
import com.lzlstudio.lzl_dinner.dao.MenuDetailDao;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class DataUpdateService extends Service {

	private static final String TAG = "DataUpdateService";
	private WorkerThread m_workerThread;
	@Override
	public void onCreate()
	{
		Log.d(TAG, "DataUpdateService::onCreate()");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "DataUpdateService::onStartCommand()");
		if(null == intent)
		{
			//killed then restarted by run time
		}
		//
		prepareMenuResource();
		//
		return Service.START_STICKY;
	}
	@Override
	public void onDestroy()
	{
		Log.d(TAG, "DataUpdateService::onDestroy()");
	}
	
	private void prepareMenuResource()
	{
		if(null == m_workerThread)
		{
			m_workerThread = new WorkerThread();
			m_workerThread.start();
		}		
	}
	
	//下载菜单资源包menu.zip
	private int downloadMenuResource()
	{
		ResourceStatusDao.getInstance().updateMenuStatus(ResourceStatusDao.STATE_DOWNLOAD);
		return 0;
	}
	//解析资源包
	private int parseMenuResource()
	{			
		ResourceStatusDao.getInstance().updateMenuStatus(ResourceStatusDao.STATE_PARSE);
		MenuLoaderHelper loader = new MenuLoaderHelper();
		int iRet = loader.Load(this);
		if(iRet >= 0)
		{
			MenuCategoryDao.getInstance().updateMenuCategory(loader.getMenuCategoryList());
			MenuDetailDao.getInstance().updateMenuDetail(loader.getMenuDetailList());
			ResourceStatusDao.getInstance().updateMenuVersion(loader.getMenuVersion());
		}
		else
		{
			ResourceStatusDao.getInstance().updateMenuStatus(ResourceStatusDao.STATE_ERROR);
		}
		return iRet;
	}
	//
	private Handler m_handler = new Handler();
	private class WorkerThread extends Thread
	{
		@Override
		public void run()
		{
			downloadMenuResource();
			parseMenuResource();
			//
			m_handler.post(new Runnable() {public void run() {stopSelf();}});
		}
	}
}
