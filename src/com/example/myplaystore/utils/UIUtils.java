package com.example.myplaystore.utils;

import com.example.myplaystore.global.GooglePlayApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

public class UIUtils {
	
	public static Context getContext(){
		return GooglePlayApplication.mContext;
	}
	
	public static int getMainThreadId(){
		return GooglePlayApplication.mMainThreadID;
	}
	
	public static Handler getHandler(){
		return GooglePlayApplication.mHandler;
	}
	
	
	public static String getString(int id){
		return getContext().getResources().getString(id);
	}
	
	public static String[] getStringArray(int id){
		return getContext().getResources().getStringArray(id);
	}
	
	public static int getColor(int id){
		return getContext().getResources().getColor(id);
	}
	
	public static ColorStateList getColorStateList(int id){
		return getContext().getResources().getColorStateList(id);
	}
	
	public static Drawable getDrawable(int id) {
		return getContext().getResources().getDrawable(id);
	}
	
	public static int getDimen(int id) {
		return getContext().getResources().getDimensionPixelSize(id);
	}
	
	// dp转像素
	public static int dip2px(float dip) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * density);
	}
	
	public static float px2dip(int px){
		float density = getContext().getResources().getDisplayMetrics().density;
		return px / density;
	}
	
	public static View getView(int id) {
		return View.inflate(getContext(), id, null);
	}
	
	public static boolean isRunOnUIThread(){
		
		int myTid = android.os.Process.myTid();
		if (myTid == getMainThreadId()) {
			return true;
		}
		return false;
	}
	
	public static void runOnUIThread(Runnable runnable){
		if (isRunOnUIThread()){
			runnable.run();
		} else {
			getHandler().post(runnable);
		}
	}
	
}
