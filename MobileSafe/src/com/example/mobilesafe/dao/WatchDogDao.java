package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.db.WatchDogOpenHelper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class WatchDogDao {
	
	private WatchDogOpenHelper openHelper;
	private Context context;
	
	public WatchDogDao(Context context){
		openHelper = new WatchDogOpenHelper(context);
		this.context = context;
	}

	public void addLockApp(String packageName){
		SQLiteDatabase sq = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packageName", packageName);
		sq.insert(WatchDogOpenHelper.DB_NAME, null, values);
		
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("Content://com.example.mobilesafe.lock.change");
		contentResolver.notifyChange(uri, null);
		sq.close();
	}
	
	public void deleteLockApp(String packageName){
		SQLiteDatabase sq = openHelper.getWritableDatabase();
		sq.delete(WatchDogOpenHelper.DB_NAME, "packagename=?", new String[]{packageName});
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("Content://com.example.mobilesafe.lock.change");
		contentResolver.notifyChange(uri, null);
		sq.close();
	}
	
	public boolean queryLockApp(String packagname){
		boolean isLock = false;
		SQLiteDatabase sq = openHelper.getWritableDatabase();
		Cursor query = sq.query(WatchDogOpenHelper.DB_NAME, null, "packagename=?", new String[]{packagname}, null, null, null);
		if (query.moveToNext()){
			isLock = true;
		}
		sq.close();
		query.close();
		
		return isLock;
	}
	
	public List<String> querLockApp(){
		List<String> list = new ArrayList<String>();
		SQLiteDatabase sq = openHelper.getReadableDatabase();
		Cursor query = sq.query(WatchDogOpenHelper.DB_NAME, new String[]{"packagename"}, null, null, null, null, null);
		while(query.moveToNext()){
			String string = query.getString(0);
			list.add(string);
		}
		sq.close();
		query.close();
		return list;
	}
}
