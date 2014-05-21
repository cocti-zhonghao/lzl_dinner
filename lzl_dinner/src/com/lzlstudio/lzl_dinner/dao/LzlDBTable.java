package com.lzlstudio.lzl_dinner.dao;

import android.database.sqlite.SQLiteDatabase;

public abstract class LzlDBTable {
	public abstract void onCreate(SQLiteDatabase sqliteDatabase);
	public abstract void onUpgrade(SQLiteDatabase sqliteDatabase, int oldversion, int newversion);
	public abstract void onOpen(SQLiteDatabase db);
	public SQLiteDatabase getDatabase()
	{
		return LzlDBOpenHelper.getInstance().getWritableDatabase();
	}
	//
	static public class NoRecordException extends Exception
	{
		private static final long serialVersionUID = -5315317626254648173L;}
}
