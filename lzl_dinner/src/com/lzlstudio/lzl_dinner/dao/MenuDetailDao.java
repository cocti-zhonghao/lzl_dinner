package com.lzlstudio.lzl_dinner.dao;

import android.database.sqlite.SQLiteDatabase;

public class MenuDetailDao extends LzlDBTable {
	private static class SingletonHolder { 
		private static final MenuDetailDao INSTANCE = new MenuDetailDao();
	}
 
	public static MenuDetailDao getInstance() {
		return SingletonHolder.INSTANCE;
	}
	//
	public final String TABLE_NAME = "lzl_menu_detail"; 	//菜品类别表
	public final String FIELD_ID = "id"; 					//INTEGER PRIMARY KEY AUTOINCREMENT;
	public final String CATEGORY_ID = "tid";				//INTEGER 
	public final String FIELD_SHORT_CUT = "shortcut";		//VARCHAR(32)
	public final String FIELD_NAME = "name";				//VARCHAR(128)
	public final String FIELD_NAME_EN = "name_en";			//VARCHAR(128)
	public final String fIELD_DESCRIPTION = "description";	//VARCHAR(512)
	private final String _CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
										 + FIELD_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
										 + CATEGORY_ID 			+ " INTEGER,"
										 + FIELD_SHORT_CUT 		+ " VARCHAR(32),"
										 + FIELD_NAME 			+ " VARCHAR(128),"
										 + FIELD_NAME_EN 		+ " VARCHAR(128),"
										 + fIELD_DESCRIPTION 	+ " VARCHAR(512)"
										 + ")";

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldversion,
			int newversion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {

	}

}
