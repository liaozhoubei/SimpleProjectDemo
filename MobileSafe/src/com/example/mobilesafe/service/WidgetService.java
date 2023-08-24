package com.example.mobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mobilesafe.R;
import com.example.mobilesafe.receiver.MyWidget;
import com.example.mobilesafe.utils.TaskUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class WidgetService extends Service {

	private AppWidgetManager mAppWidgetManager;
	private WidgetReceiver mWidgetReceiver;
	private ScreenOffReceiver mScreenOffReceiver;
	private ScreenOnReceiver mScreenOnReceiver;
	private Timer mTimer;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mAppWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		
		mWidgetReceiver = new WidgetReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.mobilesafe.service.updatewidget");
		registerReceiver(mWidgetReceiver, intentFilter);
		
		mScreenOffReceiver = new ScreenOffReceiver();
		IntentFilter screenOffFilter = new IntentFilter();
		screenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenOffReceiver, screenOffFilter);
		
		mScreenOnReceiver = new ScreenOnReceiver();
		IntentFilter screenOnfliter = new IntentFilter();
		screenOnfliter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mScreenOnReceiver, screenOnfliter);
		
		updateWidgets();
	}
	
	
	private void updateWidgets() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				ComponentName provider = new ComponentName(WidgetService.this, MyWidget.class);
				RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "正在运行的进程：" + TaskUtil.getProcessCount(WidgetService.this));
				views.setTextViewText(R.id.process_memory, "可用内存：" + Formatter.formatFileSize(WidgetService.this, TaskUtil.getAvailableRam(getApplicationContext())));
				Intent intent = new Intent();
				intent.setAction("com.example.mobilesafe.service.updatewidget");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(WidgetService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				mAppWidgetManager.updateAppWidget(provider, views);
			}
		}, 2000, 2000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopUpdateWidget();
		if (mWidgetReceiver != null) {
			unregisterReceiver(mWidgetReceiver);
		}
		if (mScreenOffReceiver != null) {
			unregisterReceiver(mScreenOffReceiver);
		}
		if (mScreenOnReceiver != null) {
			unregisterReceiver(mScreenOnReceiver);
		}
	}
	private void stopUpdateWidget() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
	// 清理全部进程广播接收者
	private class WidgetReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			killProgress();
		}

		
		
	}
	// 清理全部进程
	private void killProgress() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcessInfo : runningAppProcesses){
			if (!appProcessInfo.processName.equals(getPackageName())){
				activityManager.killBackgroundProcesses(appProcessInfo.processName);
			}
			
		}
		
	}
	// 锁屏时清理进程
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			killProgress();
			stopUpdateWidget();
		}
		
	}
	
	private class ScreenOnReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			updateWidgets();
		}
		
	}
	

}
