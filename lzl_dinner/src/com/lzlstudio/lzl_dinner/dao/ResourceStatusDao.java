package com.lzlstudio.lzl_dinner.dao;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.lzlstudio.lzl_dinner.util.DateTimeHelper;

import android.R.integer;
import android.R.string;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ResourceStatusDao extends LzlDBTable {
	private static class SingletonHolder { 
		private static final ResourceStatusDao INSTANCE = new ResourceStatusDao();
	}
 
	public static ResourceStatusDao getInstance() {
		return SingletonHolder.INSTANCE;
	}
	////
	public final String TABLE_NAME = "lzl_resource_status"; 	//菜品类别表
	public final String FIELD_ID = "id"; 					//INTEGER PRIMARY KEY AUTOINCREMENT;
	public final String FIELD_STATE = "state";				//INTEGER
	public final String FIELD_UPDATE_TIME = "update_time";	//INTEGER
	public final String FIELD_VERSION = "version";			//VARCHAR(32)
	private final String _CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
										 + FIELD_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
										 + FIELD_STATE 			+ " INTEGER,"
										 + FIELD_UPDATE_TIME 	+ " INTEGER,"
										 + FIELD_VERSION		+ " VARCHAR(32)"
										 + ")";
	////
	public final static int STATE_INVALID = 0;
	public final static int STATE_DOWNLOAD = 1;//正在网络上下载
	public final static int STATE_PARSE = 2;	//正在解析
	public final static int STATE_OK = 3;		//
	public final static int STATE_ERROR=4;
	////
	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase) {
		sqliteDatabase.execSQL(_CREATE_TABLE);
		//插入一条记录
		sqliteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(1,"+STATE_INVALID+",0,"+"\"\")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldversion,
			int newversion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {

	}

	//
	public MenuStatus getMenuStatus() throws LzlDBTable.NoRecordException
	{
		MenuStatus state = new MenuStatus();
		Cursor cursor = getDatabase().rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID +  " = 1" , null);
		if(cursor.moveToNext()){
			state.status = cursor.getInt(1);
			state.updateTime = cursor.getInt(2);
			state.updateTimeS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(state.updateTime));
			state.version = cursor.getString(3);
			cursor.close();
			return state;

		}
		else {
			cursor.close();
			throw new LzlDBTable.NoRecordException();			
		}		
	}
	
	public int updateMenuStatus(int status)
	{
		
		getDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + FIELD_STATE + " = " + status + ", " + FIELD_UPDATE_TIME + " = " + DateTimeHelper.NowSeconds() + " WHERE " + FIELD_ID + " = 1");
		return 0;
	}
	
	public int updateMenuVersion(String version)
	{
		getDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + FIELD_STATE + " = " + STATE_OK + ", " + FIELD_VERSION + " = \"" + version + "\", " + FIELD_UPDATE_TIME + " = " + DateTimeHelper.NowSeconds() + " WHERE " + FIELD_ID + " = 1");
		return 0;
	}
	
	public static class MenuStatus
	{
		public int status;
		public int updateTime;
		public String updateTimeS;
		public String version;
	}
}
