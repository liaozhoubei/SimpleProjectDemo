package com.bproject.zhihuibeijing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefUtils {
	public static void setBoolean(Context context, String key, Boolean value) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	public static boolean getBoolean(Context context, String key,Boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}
	
	
	public static void setString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public static String getString(Context context, String key,String defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		String str = sp.getString(key, defValue);
		return str;
	}
	
	
	public static void setInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	
	public static int getBoolean(Context context, String key,int defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		int value = sp.getInt(key, defValue);
		return value;
	}
	
}
