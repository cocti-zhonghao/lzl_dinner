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

import com.lzlstudio.lzl_dinner.datadefine.MenuData;

import android.content.Context;
import android.util.Log;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


public class MenuLoaderHelper {
	public MenuLoaderHelper() {}
	
	private final String TAG = "MenuLoaderHelper";
//	/**
//	* SingletonHolder is loaded on the first execution of Singleton.getInstance() 
//	* or the first access to SingletonHolder.INSTANCE, not before.
//	*/
//	private static class SingletonHolder { 
//		private static final MenuLoaderHelper INSTANCE = new MenuLoaderHelper();
//	}
// 
//	public static MenuLoaderHelper getInstance() {
//		return SingletonHolder.INSTANCE;
//	}
	private String imgBaseDir;
	//将sd卡应用目录（/Android/data/com.lzlstudio.lzl_dinner）下面的menu.zip解压到应用的私有目录下
	public int Load(Context context)
	{
		//
		imgBaseDir = "file://" + context.getFilesDir().getAbsolutePath() + "/menu/";
		//
		File dir = context.getExternalFilesDir(null);
		if(null == dir)
		{
			Log.e(TAG, "can not open exteranl storage(sd card), it has been removed or mounted on a computer, please check!!!");
			return -1;
		}
		//unzip file to /data/data/com.lzlstudio.lzl_dinner
		File toDir = context.getFilesDir();
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(new File(dir, "menu.zip"));
			zipFile.setFileNameCharset("GBK");//@zh TOOD 一般是windows系统
			//Extracts all files to the path specified
			zipFile.extractAll(toDir.getPath());
			
		} catch (ZipException e) {
			Log.e(TAG, "unzip menu resource failed: " + e.toString());
			return -1;
		}
		//
		return Parse(toDir +File.separator + "menu/index.xml");
	}
	//
	private final String XML_TAG_ROOT = "menu";
	private final String XML_TAG_category = "category";
	private final int XML_TAG_FLAG_category = 1;
	private final String XML_TAG_item = "item";
	private final String XML_TAG_item_des = "description";
	private final int XML_TAG_FLAG_0 = 0;
	//解析菜单资源
	private int Parse(String filename)
	{
		menuCategoryList.clear();
		menuCategoryDetailList.clear();
		//
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
			return -1;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "menu resource (XML) parse error: " + e.toString());
			return -1;
		} catch (IOException e) {
			Log.e(TAG, "menu resource (XML) parse error: " + e.toString());
			return -1;
		}        
		//@zh for test
		Log.d(TAG, "======================after parse===================");
		for (MenuData.MenuCategoryItem item : menuCategoryList) {
			Log.d(TAG, "title: "+item.title + ", img: " + item.img + ", key: " + item.key);
		}
		//
	    for (MenuData.MenuItem item : menuCategoryDetailList) {
	    	Log.d(TAG, "categoryID: " + item.category_id + ", title: " + item.title + ", img: " + item.img + "original_price: " + item.original_price + ", special_price: " + item.special_price + ", price: " + item.price);
		}
	    //
	    return 0;
	}	
	//
	private int category_id = 0;
	private int START_TAG_FLAG = XML_TAG_FLAG_0;
	ArrayList<MenuData.MenuCategoryItem> menuCategoryList = new ArrayList<MenuData.MenuCategoryItem>();
	ArrayList<MenuData.MenuItem> menuCategoryDetailList = new ArrayList<MenuData.MenuItem>();
	MenuData.MenuItem nextMenuItem;
	private void parseStartTag(XmlPullParser xpp)
	{
		String tag = xpp.getName();
		if(tag.equals(XML_TAG_ROOT))
		{
			m_menuVersion = xpp.getAttributeValue(0);
		}
		else if(tag.equals(XML_TAG_category))
		{
			START_TAG_FLAG = XML_TAG_FLAG_category;
			++category_id;
			//菜品类别列表项
			MenuData.MenuCategoryItem item = new MenuData.MenuCategoryItem();
			item.title = xpp.getAttributeValue(0);
			item.img = imgBaseDir+xpp.getAttributeValue(1);
			item.id = category_id;
			//
			item.selected_img = xpp.getAttributeValue(null, "select_img");
			if(item.selected_img == null) item.selected_img = item.img;
			else item.selected_img = imgBaseDir+item.selected_img;
			menuCategoryList.add(item);
		}
		else if(tag.equals(XML_TAG_item))
		{
			if(XML_TAG_FLAG_category == START_TAG_FLAG)
			{
				//某一类菜品列表项
				nextMenuItem = new MenuData.MenuItem();
				nextMenuItem.category_id = category_id;
				nextMenuItem.title = xpp.getAttributeValue(0);
				nextMenuItem.img = imgBaseDir+xpp.getAttributeValue(1);
				String price = xpp.getAttributeValue(2);
				nextMenuItem.original_price = (null == price || price.isEmpty()) ? 0 : Double.parseDouble(price);
				price = xpp.getAttributeValue(3);
				nextMenuItem.special_price = (null == price || price.isEmpty()) ? 0 : Double.parseDouble(price);
				price = xpp.getAttributeValue(4);
				nextMenuItem.price = (null == price || price.isEmpty()) ? 0 : Double.parseDouble(price);
				//
				menuCategoryDetailList.add(nextMenuItem);
			}
		}
		else if(tag.equals(XML_TAG_item_des))
		{
			if(XML_TAG_FLAG_category == START_TAG_FLAG)
			{
				nextMenuItem.des = xpp.getText(); if(null == nextMenuItem.des) nextMenuItem.des = "";
			}			
		}
	}
	private void parseEndTag(XmlPullParser xpp)
	{
		String tag = xpp.getName();
		if(tag.equals(XML_TAG_category))
		{
			START_TAG_FLAG = XML_TAG_FLAG_0;
		}
		else if(tag.equals(XML_TAG_item))
		{
			if(XML_TAG_FLAG_category == START_TAG_FLAG)
			{
				nextMenuItem = null;
			}			
		}
	}
	//
	private String m_menuVersion;
	public String getMenuVersion() {return m_menuVersion;}
	public ArrayList<MenuData.MenuCategoryItem> getMenuCategoryList() {return menuCategoryList;}
	public ArrayList<MenuData.MenuItem> getMenuDetailList() {return menuCategoryDetailList;}
}
