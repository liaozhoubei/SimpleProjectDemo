package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppEngine {
	public static List<AppInfo> getAppInfos(Context context) {
		List<AppInfo> list = new ArrayList<AppInfo>();
		PackageManager manager = context.getPackageManager();
		List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
		for(PackageInfo packageInfo : installedPackages) {
			String packageName = packageInfo.packageName;
			String versionName = packageInfo.versionName;
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			Drawable loadIcon = applicationInfo.loadIcon(manager);
			String loadLabel = applicationInfo.loadLabel(manager).toString();
			boolean isUser;
			int flags = applicationInfo.flags;
			if ((applicationInfo.FLAG_SYSTEM & flags) == ApplicationInfo.FLAG_SYSTEM){
				isUser = false;
			}else{
				//用户程序
				isUser = true;
			}
			boolean isSD;
			if ((applicationInfo.FLAG_EXTERNAL_STORAGE & flags) == ApplicationInfo.FLAG_EXTERNAL_STORAGE){
				//安装到了SD卡
				isSD = true;
			}else{
				//安装到手机中
				isSD = false;
			}
			AppInfo info = new AppInfo(loadLabel, loadIcon, packageName, versionName, isSD, isUser);
			list.add(info);
		}
		
		return list;
		
	}

}
