package com.lzlstudio.lzl_dinner.dao;

import com.lzlstudio.lzl_dinner.MyApplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LzlDBOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "lzldinner.db";
	private static final int DATABASE_VERSION = 1;
	// 
	private static class SingletonHolder { 
		private static final LzlDBOpenHelper INSTANCE = new LzlDBOpenHelper(MyApplication.INSTANCE);
	}
 
	public static LzlDBOpenHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}

	
	private LzlDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		MenuCategoryDao.getInstance().onCreate(db);
		MenuDetailDao.getInstance().onCreate(db);
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	}

	//
	@Override
	public void onOpen(SQLiteDatabase db)
	{}
}
