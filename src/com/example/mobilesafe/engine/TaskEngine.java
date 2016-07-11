package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

public class TaskEngine {
	public static List<TaskInfo> getTaskAllInfo(Context context) {
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager packageManager = context.getPackageManager();
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		for(RunningAppProcessInfo processInfo : runningAppProcesses){
			TaskInfo taskInfo = new TaskInfo();
			String processName = processInfo.processName;
			taskInfo.setName(processName);
			MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});
			int totalPss = processMemoryInfo[0].getTotalPss();
			int romSize = totalPss*1024;
			taskInfo.setRomSize(romSize);
			try {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);
				Drawable icon = applicationInfo.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				String appname = applicationInfo.loadLabel(packageManager).toString();
				taskInfo.setName(appname);
				int flags = applicationInfo.flags;
				boolean isUser;
				if ((applicationInfo.flags & flags) == applicationInfo.flags){
					isUser = false;
				} else {
					isUser = true;
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}

}
