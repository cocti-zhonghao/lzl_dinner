package com.lzlstudio.lzl_dinner.dao;

import android.database.sqlite.SQLiteDatabase;

public abstract class LzlDBTable {
	public abstract void onCreate(SQLiteDatabase sqliteDatabase);
	public abstract void onUpgrade(SQLiteDatabase sqliteDatabase, int oldversion, int newversion);
	public abstract void onOpen(SQLiteDatabase db);
	//
}
