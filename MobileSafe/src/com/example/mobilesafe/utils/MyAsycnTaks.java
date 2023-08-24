package com.example.mobilesafe.utils;

import android.os.Handler;

public abstract class MyAsycnTaks {
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			postTast();
		};
		
	};
	
	
	public abstract void preTask();
	
	public abstract void doingTast();
	
	public abstract void postTast();
	
	public void excute(){
		preTask();
		
		new Thread(){
			public void run() {
				doingTast();
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}
}
