package com.example.mobilesafe.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 查询应用签名是否在应用黑名单中，如果有返回true
 * @author ASUS-H61M
 *
 */
public class AntiVirusDao {
	/**
	 * 
	 * @param context
	 * @param signature 应用签名信息
	 * @return 返回布尔值，在黑名单中返回true
	 */
	public static boolean queryAntiVirus(Context context, String signature){
		boolean ishave = false;
		File file = new File(context.getFilesDir(), "antivirus.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		Cursor query = database.query("datable", null, "md5=?", new String[]{signature}, null, null, null, null);
		if (query.moveToNext()){
			ishave = true;
		}
		database.close();
		query.close();
		return ishave;
	}
}
