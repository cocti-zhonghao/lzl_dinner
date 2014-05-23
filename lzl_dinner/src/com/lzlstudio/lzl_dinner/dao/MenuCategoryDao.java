package com.lzlstudio.lzl_dinner.dao;

import java.util.ArrayList;

import com.lzlstudio.lzl_dinner.datadefine.MenuData;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class MenuCategoryDao extends LzlDBTable {

	private static class SingletonHolder { 
		private static final MenuCategoryDao INSTANCE = new MenuCategoryDao();
	}
 
	public static MenuCategoryDao getInstance() {
		return SingletonHolder.INSTANCE;
	}
	//
	public final String TABLE_NAME = "lzl_menu_category"; 		//菜品类别表
	public final String FIELD_ID = "id"; 						//INTEGER PRIMARY KEY ;
	public final String FIELD_NAME = "name";					//VARCHAR(128)
	public final String FIELD_NAME_EN = "name_en";				//VARCHAR(128)
	public final String FIELD_DESCRIPTION = "description";		//VARCHAR(512)
	public final String FIELD_IMG_URL = "img_url";				//VARCHAR(128)
	public final String FIELD_SELECT_IMG_URL = "select_img_url";//VARCHAR(128)
	private final String _CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
										 + FIELD_ID 			+ " INTEGER PRIMARY KEY ,"
										 + FIELD_NAME 			+ " VARCHAR(128),"
										 + FIELD_NAME_EN		+ " VARCHAR(128),"
										 + FIELD_DESCRIPTION 	+ " VARCHAR(512),"
										 + FIELD_IMG_URL		+ " VARCHAR(128),"
										 + FIELD_SELECT_IMG_URL + " VARCHAR(128)"
										 + ")";
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase) {
		sqliteDatabase.execSQL(_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldversion,
			int newversion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {

	}
	//
	public int updateMenuCategory(ArrayList<MenuData.MenuCategoryItem> categoryList)
	{
		int iRet = 0;
		SQLiteDatabase sqliteDatabase = getDatabase();
		SQLiteStatement sqlListStatment = sqliteDatabase.compileStatement("INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?)");
		//
		sqliteDatabase.beginTransaction();
		//
		sqliteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
		//
		for (MenuData.MenuCategoryItem item : categoryList) {
			sqlListStatment.bindLong(1, item.id);
			sqlListStatment.bindString(2, item.title);
			sqlListStatment.bindString(3, item.title_en);
			sqlListStatment.bindString(4, item.des);
			sqlListStatment.bindString(5, item.img);
			sqlListStatment.bindString(6, item.selected_img);
			//
			long rowId = sqlListStatment.executeInsert();
			if (rowId < 0 ) {
				iRet = -1;
			}
		}
		//
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return iRet;
	}
	//
	public ArrayList<MenuData.MenuCategoryItem> getMenuCategory()
	{
		//
		long before = System.currentTimeMillis();
		Log.e("DB", "before getMenuCategory: " + before);
		//
		ArrayList<MenuData.MenuCategoryItem> list = new ArrayList<MenuData.MenuCategoryItem>();
		Cursor cursor = getDatabase().rawQuery(" SELECT * FROM " + TABLE_NAME + " ORDER BY " + FIELD_ID , null);
		while(cursor.moveToNext()){
			MenuData.MenuCategoryItem item = new MenuData.MenuCategoryItem();
			item.id = cursor.getInt(0);
			item.title = cursor.getString(1);
			item.title_en = cursor.getString(2); if(item.title_en == null) item.title_en = "" ;
			item.des = cursor.getString(3); if(item.des == null) item.des = "";
			item.img = cursor.getString(4);
			item.selected_img = cursor.getString(5);
			list.add(item);
		}
		//
		long after = System.currentTimeMillis();
		Log.e("DB", "after getMenuCategory: " + after + ", use " + (after - before));
		//
		return list;
	}
}
