package com.example.mobilesafe.service;

import java.util.List;

import com.example.mobilesafe.WatchDogActivity;
import com.example.mobilesafe.dao.WatchDogDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * 软件密码锁服务
 * 
 * @author ASUS-H61M
 *
 */
public class WatchDogService extends Service {

	private WatchDogDao watchDogDao;
	private boolean isLock;
	private UnlockCurrentReceiver unlockCurrentReceiver;
	private String unlockPackageName;
	private ScreenOffReceiver screenOffReceiver;
	private List<String> list;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isLock = true;
		// 接收从WatchDogActivity传过来的解锁广播
		unlockCurrentReceiver = new UnlockCurrentReceiver();
		IntentFilter unLockIntentFilter = new IntentFilter();
		unLockIntentFilter.addAction("com.example.mobilesafe.watchdogactivity.unlock");
		registerReceiver(unlockCurrentReceiver, unLockIntentFilter);
		
		screenOffReceiver = new ScreenOffReceiver();
		IntentFilter screenOffIntentFilter = new IntentFilter();
		screenOffIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenOffReceiver, screenOffIntentFilter);
		
		watchDogDao = new WatchDogDao(getApplicationContext());
		// 使用内容观察者，在数据发生更改的时候更改list的内容
		Uri uri = Uri.parse("Content://com.example.mobilesafe.lock.change");
		getContentResolver().registerContentObserver(uri, true, new ContentObserver(null) {
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				list = watchDogDao.querLockApp();
			}
		});
		// 将数据存在内存中，防止过多占用系统资源
		list = watchDogDao.querLockApp();
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {
				while (isLock) {
					
					// getRunningTasks() 此方法在Android Lollipop及之后的版本技能能用与debug上
					List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
					for (RunningTaskInfo info : runningTasks) {
						ComponentName componentName = info.baseActivity;
						String packageName = componentName.getPackageName();
						boolean contains = list.contains(packageName);
						if ( contains ) {
							if ( !packageName.equals(unlockPackageName)){
								Intent intent = new Intent(WatchDogService.this, WatchDogActivity.class);
								intent.putExtra("packageName", packageName);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						}
					}
					SystemClock.sleep(3 * 100);
				}

			};
		}.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isLock = false;
		if (unlockCurrentReceiver != null) {
			unregisterReceiver(unlockCurrentReceiver);
			unlockCurrentReceiver = null;
		} 
		if (screenOffReceiver != null) {
			unregisterReceiver(screenOffReceiver);
			screenOffReceiver = null;
		}
	}
	
	// 接收解锁的广播
	private class UnlockCurrentReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			unlockPackageName = intent.getStringExtra("packageName");
			
		}
		
	}
	
	// 锁屏的时候将传过来的包名置空
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			unlockPackageName = null;
		}
		
	}

}
