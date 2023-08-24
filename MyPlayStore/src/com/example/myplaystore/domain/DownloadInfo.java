package com.example.myplaystore.domain;

import java.io.File;

import com.example.myplaystore.manager.DownloadManager;

import android.R.string;
import android.os.Environment;

public class DownloadInfo {

	public String id;
	public String name;
	public String downloadUrl;
	public long size;
	public String packageName;

	public long currentPos;// 当前下载位置
	public int currentState;// 当前下载状态
	public String path;// 下载到本地文件的路径

	public static final String PLAY_STORE = "PLAYSTORE";// sdcard根目录文件夹名称
	public static final String DONWLOAD = "download";// 子文件夹名称, 存放下载的文件
	
	public float getProgress() {
		if (size == 0) {
			return 0;
		}

		float progress = currentPos / (float) size;
		return progress;
	}
	
	public static DownloadInfo copy(AppInfo info) {
		DownloadInfo downloadInfo = new DownloadInfo();

		downloadInfo.id = info.id;
		downloadInfo.name = info.name;
		downloadInfo.downloadUrl = info.downloadUrl;
		downloadInfo.packageName = info.packageName;
		downloadInfo.size = info.size;

		downloadInfo.currentPos = 0;
		downloadInfo.currentState = DownloadManager.STATE_UNDO;// 默认状态是未下载
		downloadInfo.path = downloadInfo.getFilePath();

		return downloadInfo;
	}

	private String getFilePath() {
		StringBuffer stringBuffer = new StringBuffer();
		String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
		stringBuffer.append(sdcard);
		stringBuffer.append(File.separator);
		stringBuffer.append(PLAY_STORE);
		stringBuffer.append(File.separator);
		stringBuffer.append(DONWLOAD);
		if (createDir(stringBuffer.toString())) {
			return stringBuffer.toString() + File.separator + name + "apk";
		}
		
		return null;
	}

	private boolean createDir(String string) {
		File file = new File(string);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		} 

		return true;
	}
}
