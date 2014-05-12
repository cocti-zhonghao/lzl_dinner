package com.lzlstudio.lzl_dinner;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public class MenuLoaderHelper {
	private MenuLoaderHelper() {}
	
	private final String TAG = "MenuLoaderHelper";
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
			Log.e(TAG, "can not open exteranl storage(sd card), it has been removed or mounted on a computer, please check!!!");
			return;
		}
		//unzip file to /data/data/com.lzlstudio.lzl_dinner
		File toDir = context.getFilesDir();
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(new File(dir, "menu.zip"));		
			//ZipFile zipFile = new ZipFile(dir.getAbsolutePath()+File.separator+"menu.zip");
			//Extracts all files to the path specified
			zipFile.extractAll(toDir.getPath());
			
		} catch (ZipException e) {
			Log.e(TAG, "unzip menu resource failed: " + e.toString());
			return;
		}
		//
		Parse(toDir +File.separator + "menu/index.xml");
	}
	//
	private final String XML_TAG_category_list = "category_list";
	private final int XML_TAG_FLAG_category_list = 1;
	private final String XML_TAG_item = "item";
	private final String XML_TAG_category_content = "category_content";
	private final int XML_TAG_FLAG_category_content = 2;
	private final int XML_TAG_FLAG_0 = 0;
	//解析菜单资源
	private void Parse(String filename)
	{
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();			
			xpp.setInput(new FileInputStream(new File(filename)), "UTF-8");
			//
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) 
			{
				if(eventType == XmlPullParser.START_DOCUMENT) 
				{
				} 
				else if(eventType == XmlPullParser.START_TAG) 
				{
					parseStartTag(xpp);
				} 
				else if(eventType == XmlPullParser.END_TAG) 
				{
					parseEndTag(xpp);
				} 
				else if(eventType == XmlPullParser.TEXT) 
				{
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			Log.e(TAG, "menu resource (XML) parse error: " + e.toString());
		} catch (FileNotFoundException e) {
			Log.e(TAG, "menu resource (XML) parse error: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "menu resource (XML) parse error: " + e.toString());
		}        
		//@zh for test
		Log.d(TAG, "======================after parse===================");
		for (MenuCategoryItem item : menuCategoryList) {
			Log.d(TAG, "title: "+item.title + ", img: " + item.img + ", key: " + item.key);
		}
		//
		Iterator<Entry<String, ArrayList<MenuItem>>> iter = menu.entrySet().iterator(); 
		while (iter.hasNext()) 
		{ 
		    Map.Entry<String, ArrayList<MenuItem>> entry = (Map.Entry<String, ArrayList<MenuItem>>) iter.next(); 
		    String key = entry.getKey(); 
		    ArrayList<MenuItem> val = entry.getValue();
		    Log.d(TAG, "===================category key: " + key + " ====================");
		    for (MenuItem item : val) {
		    	Log.d(TAG, "title: " + item.title + ", img: " + item.img + "original_price: " + item.original_price + ", special_price: " + item.special_price + ", price: " + item.price);
			}
		    //
		}
	}	
	//
	private int START_TAG_FLAG = XML_TAG_FLAG_0;
	ArrayList<MenuCategoryItem> menuCategoryList = new ArrayList<MenuCategoryItem>();
	ArrayList<MenuItem> menuCategoryDetailList;
	String menuCategoryKey;
	Map<String, ArrayList<MenuItem>> menu = new HashMap<String, ArrayList<MenuItem>>();
	private void parseStartTag(XmlPullParser xpp)
	{
		String tag = xpp.getName();
		if(tag.equals(XML_TAG_category_list))
		{
			START_TAG_FLAG = XML_TAG_FLAG_category_list;
		}
		else if(tag.equals(XML_TAG_category_content))
		{
			START_TAG_FLAG = XML_TAG_FLAG_category_content;
			menuCategoryDetailList = new ArrayList<MenuItem>();
			menuCategoryKey = xpp.getAttributeValue(0);
		}
		else if(tag.equals(XML_TAG_item))
		{
			if(XML_TAG_FLAG_category_list == START_TAG_FLAG)
			{
				//菜品类别列表项
				MenuCategoryItem item = new MenuCategoryItem();
				item.title = xpp.getAttributeValue(0);
				item.img = xpp.getAttributeValue(1);
				item.key = xpp.getAttributeValue(2);
				menuCategoryList.add(item);
			}
			else if(XML_TAG_FLAG_category_content == START_TAG_FLAG)
			{
				//某一类菜品列表项
				MenuItem item = new MenuItem();
				item.title = xpp.getAttributeValue(0);
				item.img = xpp.getAttributeValue(1);
				String price = xpp.getAttributeValue(2);
				item.original_price = (null == price || price.isEmpty()) ? 0 : Double.parseDouble(price);
				price = xpp.getAttributeValue(3);
				item.special_price = (null == price || price.isEmpty()) ? 0 : Double.parseDouble(price);
				price = xpp.getAttributeValue(4);
				item.price = (null == price || price.isEmpty()) ? 0 : Double.parseDouble(price);
				//
				menuCategoryDetailList.add(item);
			}
		}
	}
	private void parseEndTag(XmlPullParser xpp)
	{
		String tag = xpp.getName();
		if(tag.equals(XML_TAG_category_list))
		{
			START_TAG_FLAG = XML_TAG_FLAG_0;
		}
		else if(tag.equals(XML_TAG_category_content))
		{
			START_TAG_FLAG = XML_TAG_FLAG_0;
			if(null != menuCategoryDetailList && menuCategoryDetailList.size() > 0)
			{
				menu.put(menuCategoryKey, menuCategoryDetailList);
			}
			//
			menuCategoryDetailList = null;
			menuCategoryKey = null;
		}
	}
	//////
	static private class MenuCategoryItem
	{
		public String title;
		public String img;
		public String key;
	}
	
	static private class MenuItem
	{
		public String title;
		public String img;
		public String key;
		public double original_price;
		public double special_price;
		public double price;
	}
}
