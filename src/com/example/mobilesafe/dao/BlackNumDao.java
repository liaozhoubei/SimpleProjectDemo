package com.example.mobilesafe.dao;

import com.example.mobilesafe.db.BlackNumOpenHlper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumDao {
	
	public static final int CALL=0;
	public static final int SMS=1;
	public static final int ALL=2;
	private BlackNumOpenHlper mBlackNumOpenHlper;
	
	public BlackNumDao(Context context) {
		mBlackNumOpenHlper = new BlackNumOpenHlper(context);
	}
	
	public void addBlackNum(String blacknum, int mode) {
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("blacknum", blacknum);
		values.put("mode", mode);
		sqLiteDatabase.insert(BlackNumOpenHlper.DB_NAME, null, values);
		sqLiteDatabase.close();
	}
	
	public void updateBlackNum(String blacknum,int mode) {
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		sqLiteDatabase.update(BlackNumOpenHlper.DB_NAME, values, "blacknum=?", new String[]{blacknum});
		sqLiteDatabase.close();
		
//		ContentValues values = new ContentValues();
//		values.put("mode", mode);
//		sqLiteDatabase.update(BlackNumOpenHlper.DB_NAME, values, "blacknum=?", new String[]{blacknum});
//		//3.关闭数据库
//		sqLiteDatabase.close();
	}
	
	public int queryBlackNumMode(String blacknum){
		int mode = -1;
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		Cursor cursor = sqLiteDatabase.query(BlackNumOpenHlper.DB_NAME, new String[]{"mode"}, "blacknum=?", new String[]{blacknum}, null, null, null);
		if (cursor.moveToNext()){
			mode = cursor.getInt(0);
		}
		cursor.close();
		sqLiteDatabase.close();
		return mode;
		
	}
	
	
	public void deleteBlackNum(String blacknum) {
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		sqLiteDatabase.delete(BlackNumOpenHlper.DB_NAME, "blacknum=?", new String[]{blacknum});
		sqLiteDatabase.close();
	}
}
