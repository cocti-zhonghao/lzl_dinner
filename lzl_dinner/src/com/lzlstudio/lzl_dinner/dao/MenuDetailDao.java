package com.lzlstudio.lzl_dinner.dao;

import java.util.ArrayList;

import com.lzlstudio.lzl_dinner.datadefine.MenuData;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class MenuDetailDao extends LzlDBTable {
	private static class SingletonHolder { 
		private static final MenuDetailDao INSTANCE = new MenuDetailDao();
	}
 
	public static MenuDetailDao getInstance() {
		return SingletonHolder.INSTANCE;
	}
	//
	public final String TABLE_NAME = "lzl_menu_detail"; 	//菜品详情表
	public final String FIELD_ID = "id"; 					//INTEGER PRIMARY KEY AUTOINCREMENT;
	public final String CATEGORY_ID = "category_id";		//INTEGER 
	public final String FIELD_SHORT_CUT = "shortcut";		//VARCHAR(32)
	public final String FIELD_NAME = "name";				//VARCHAR(128)
	public final String FIELD_NAME_EN = "name_en";			//VARCHAR(128)
	public final String FIELD_DESCRIPTION = "description";	//VARCHAR(512)
	public final String FIELD_IMG_URL = "img_url";			//VARCHAR(128)
	public final String FIELD_O_PRICE = "original_price";	//FLOAT
	public final String FIELD_S_PRICE = "special_price";	//FLOAT
	public final String FIELD_PRICE = "price";				//FLOAT
	private final String _CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
										 + FIELD_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
										 + CATEGORY_ID 			+ " INTEGER,"
										 + FIELD_SHORT_CUT 		+ " VARCHAR(32),"
										 + FIELD_NAME 			+ " VARCHAR(128),"
										 + FIELD_NAME_EN 		+ " VARCHAR(128),"
										 + FIELD_DESCRIPTION 	+ " VARCHAR(512),"
										 + FIELD_IMG_URL		+ " VARCHAR(128),"
										 + FIELD_O_PRICE		+ " FLOAT,"
										 + FIELD_S_PRICE		+ " FLOAT,"
										 + FIELD_PRICE			+ " FLOAT"
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

	public int updateMenuDetail(ArrayList<MenuData.MenuItem> detailList)
	{
		int iRet = 0;
		SQLiteDatabase sqliteDatabase = getDatabase();
		SQLiteStatement sqlListStatment = sqliteDatabase.compileStatement("INSERT INTO " + TABLE_NAME + "("
																		  + CATEGORY_ID + ","
																		  + FIELD_SHORT_CUT + ","
																		  + FIELD_NAME + ","
																		  + FIELD_NAME_EN + ","
																		  + FIELD_DESCRIPTION + ","
																		  + FIELD_IMG_URL + ","
																		  + FIELD_O_PRICE + ","
																		  + FIELD_S_PRICE + ","
																		  + FIELD_PRICE + ")"
																		  + " VALUES(?,?,?,?,?,?,?,?,?)");
		//
		sqliteDatabase.beginTransaction();
		//
		sqliteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
		//
		for (MenuData.MenuItem item : detailList) {
			sqlListStatment.bindLong(1, item.category_id);
			sqlListStatment.bindString(2, item.short_cut);
			sqlListStatment.bindString(3, item.title);
			sqlListStatment.bindString(4, item.title_en);
			sqlListStatment.bindString(5, item.des);
			sqlListStatment.bindString(6, item.img);
			sqlListStatment.bindDouble(7, item.original_price);
			sqlListStatment.bindDouble(8, item.special_price);
			sqlListStatment.bindDouble(9, item.price);
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
	
	public ArrayList<MenuData.MenuItem> getMenuItemList(int category_id)
	{
		ArrayList<MenuData.MenuItem> list = new ArrayList<MenuData.MenuItem>();
		Cursor cursor = getDatabase().rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + CATEGORY_ID + " = " + category_id + " ORDER BY " + FIELD_ID , null);
		while(cursor.moveToNext()){
			MenuData.MenuItem item = new MenuData.MenuItem();
			item.id = cursor.getInt(0);
			item.title = cursor.getString(3);
			item.title_en = cursor.getString(4); if(item.title_en == null) item.title_en = "" ;
			item.des = cursor.getString(5); if(item.des == null) item.des = "";
			item.img = cursor.getString(6);
			item.original_price = cursor.getDouble(7);
			item.special_price = cursor.getDouble(8);
			item.price = cursor.getDouble(9);
			
			list.add(item);
		}
		return list;
	}
}
