package com.example.mobilesafe.service;

import java.util.List;

import com.example.mobilesafe.MainActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
/**
 * 软件密码锁服务
 * @author ASUS-H61M
 *
 */
public class WatchDogService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		new Thread(){
			@SuppressWarnings("deprecation")
			public void run() {
				getActivePackagesCompat();
				SystemClock.sleep(3 * 1000);
			};
		}.start();
	}
	
	
	
	// 但系统版本低于Android Lollipop用用此方法
	private void getActivePackagesCompat() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// getRunningTasks() 此方法在Android Lollipop及之后的版本技能能用与debug上
		List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(3);
		for( RunningTaskInfo info : runningTasks){
			ComponentName componentName  = info.baseActivity;
			String packageName = componentName.getPackageName();
			if (packageName.equals("com.android.mms")){
				Intent intent = new Intent(WatchDogService.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
			}
		}
	}
	

}
