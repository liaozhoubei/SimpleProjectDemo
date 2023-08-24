package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.BlackNumInfo;
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

	}
	
	// 查询单个黑名单
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
	// 查询所有黑名单
	public List<BlackNumInfo> queryAllBlackNumInfo(){
		List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		Cursor cursor = sqLiteDatabase.query(BlackNumOpenHlper.DB_NAME, new String[]{"blacknum", "mode"}, null, null, null, null, "_id desc");
		while(cursor.moveToNext()){
			String blacknum = cursor.getString(0);
			int mode = cursor.getInt(1);
			BlackNumInfo info = new BlackNumInfo(blacknum, mode);
			list.add(info);
		}
		cursor.close();
		sqLiteDatabase.close();
		return list;
	}
	
	// 分批查询黑名单
	public List<BlackNumInfo> queryPartBlackNumInfo(int startNum, int endNum) {
		List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery("select blacknum, mode from info order by _id desc limit ? offset ?", new String[]{endNum + "", startNum + ""});
		while (cursor.moveToNext()){
			String blacknum = cursor.getString(0);
			int mode = cursor.getInt(1);
			BlackNumInfo info = new BlackNumInfo(blacknum, mode);
			list.add(info);
		}
		cursor.close();
		sqLiteDatabase.close();
		return list;
	}
	
	
	public void deleteBlackNum(String blacknum) {
		SQLiteDatabase sqLiteDatabase = mBlackNumOpenHlper.getWritableDatabase();
		sqLiteDatabase.delete(BlackNumOpenHlper.DB_NAME, "blacknum=?", new String[]{blacknum});
		sqLiteDatabase.close();
	}
}
