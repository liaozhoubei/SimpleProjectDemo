package com.example.myplaystore.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class GooglePlayApplication extends Application {
	
	public static Context mContext;
	public static Handler mHandler;
	public static int mMainThreadID;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mHandler = new Handler();
		mMainThreadID = android.os.Process.myTid();
	}

	public static Context getmContext() {
		return mContext;
	}

	public static Handler getmHandler() {
		return mHandler;
	}

	public static int getmMainThreadID() {
		return mMainThreadID;
	}

	
}
