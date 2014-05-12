package com.lzlstudio.lzl_dinner.dao;

import android.database.sqlite.SQLiteDatabase;

public class MenuCategoryDao extends LzlDBTable {

	private static class SingletonHolder { 
		private static final MenuCategoryDao INSTANCE = new MenuCategoryDao();
	}
 
	public static MenuCategoryDao getInstance() {
		return SingletonHolder.INSTANCE;
	}
	//
	public final String TABLE_NAME = "lzl_menu_category"; 	//菜品类别表
	public final String FIELD_ID = "id"; 					//INTEGER PRIMARY KEY AUTOINCREMENT;
	public final String FIELD_NAME = "name";				//VARCHAR(128)
	public final String FIELD_NAME_EN = "name_en";			//VARCHAR(128)
	public final String fIELD_DESCRIPTION = "description";	//VARCHAR(512)
	private final String _CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
										 + FIELD_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
										 + FIELD_NAME 			+ " VARCHAR(128),"
										 + fIELD_DESCRIPTION 	+ " VARCHAR(512)"
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
}
