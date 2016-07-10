package com.example.mobilesafe.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class AppUtil {
	@SuppressWarnings("deprecation")
	public static long getAvailableSD(){
		File path = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSize();
		long blockCount = statFs.getBlockCount();
		long availableBlocks = statFs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static long getAvailableROM(){
		File dataDirectory = Environment.getDataDirectory();
		StatFs statFs = new StatFs(dataDirectory.getPath());
		long blockSize = statFs.getBlockSize();
		long blockCount = statFs.getBlockCount();
		long availableBlocks = statFs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
}
