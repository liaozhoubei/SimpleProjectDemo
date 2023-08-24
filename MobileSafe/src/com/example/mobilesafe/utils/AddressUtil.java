package com.example.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;

/**
 * 动态获取进程名称
 * @author Bei
 *
 */
public class AddressUtil {
	/**
	 * 动态获取服务是否开启
	 * @param className 传入需要获取的服务的全类名
	 * @param context
	 * @return
	 */
	public static boolean isRunningServer(String className, Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
		for (RunningServiceInfo runningServiceInfo : runningServices){
			ComponentName service = runningServiceInfo.service;
			String className2 = service.getClassName();
			if (className2.equals(className)) {
				return true;
			}
		}
		
		return false;
	}
}
