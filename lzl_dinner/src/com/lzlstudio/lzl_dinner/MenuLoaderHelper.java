package com.lzlstudio.lzl_dinner;


import java.io.File;

import android.content.Context;
import android.util.Log;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public class MenuLoaderHelper {
	private MenuLoaderHelper() {}
	
	/**
	* SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	* or the first access to SingletonHolder.INSTANCE, not before.
	*/
	private static class SingletonHolder { 
		private static final MenuLoaderHelper INSTANCE = new MenuLoaderHelper();
	}
 
	public static MenuLoaderHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}
	//将sd卡应用目录（/Android/data/com.lzlstudio.lzl_dinner）下面的menu.zip解压到应用的私有目录下
	public void Load(Context context)
	{
		File dir = context.getExternalFilesDir(null);
		if(null == dir)
		{
			Log.e("MenuLoaderHelper", "can not open exteranl storage(sd card), it has been removed or mounted on a computer, please check!!!");
			return;
		}
		//unzip file to /data/data/com.lzlstudio.lzl_dinner
		File toDir = context.getFilesDir();
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(new File(dir, "menu.zip"));		
			//ZipFile zipFile = new ZipFile(dir.getAbsolutePath()+File.separator+"menu.zip");
			// Extracts all files to the path specified
			zipFile.extractAll(toDir.getPath());
			
		} catch (ZipException e) {
			Log.e("MenuLoaderHelper", "unzip menu resource failed: " + e.toString());
			return;
		}
		//
		Parse();
	}
	
	//解析菜单资源
	private void Parse()
	{
		
	}
}
