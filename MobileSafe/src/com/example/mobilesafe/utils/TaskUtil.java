package com.example.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class TaskUtil {
	// 获得运行的程序个数
	/**
	 * 获得所有正在运行的程序个数
	 * @param context
	 * @return
	 */
	public static int getProcessCount(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		return runningAppProcesses.size();
	}
	// 
	/**
	 * 获取可用内存大小
	 * @param context
	 * @return
	 */
	public static long getAvailableRam(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	/**
	 * 低于16版本的系统，直接读取文件中的内存信息
	 * @param context
	 * @return
	 * @deprecated
	 */
	public static long getTotalRam() {
		File file = new File("/proc/meminfo");
		StringBuilder builder = new StringBuilder();
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String readLine = br.readLine();
			char[] charArray = readLine.toCharArray();
			for (char c : charArray){
				if (c >= '0' && c <='9'){
					builder.append(c);
				}
			}
			String str = builder.toString();
			long parseLong = Long.parseLong(str);
			return parseLong * 1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	/**
	 * 获取全部内存
	 * @param context
	 * @return
	 */
	public static long getTotalRam(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		// 使用16版本以上的系统能获取
		return outInfo.totalMem; 
	}
	
	
	
}
