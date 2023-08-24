package com.bproject.zhihuibeijing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 本地缓存图片的工具类
 * @author ASUS-H61M
 *
 */
public class LocalCacheUtils {
	public static final String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/zhihuibeijing_cahce";
	
	/**
	 * 设置图片本地缓存
	 * @param url	传入图片的下载地址，经过MD5加密后成为文件名
	 * @param bitmap	传入已经下载好的图片
	 */
	public void setLocalCache(String url, Bitmap bitmap){
		File dir = new File(LOCAL_CACHE_PATH);
		if (!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
		
		try {
			String fileName = MD5Encoder.encode(url);
			File file = new File(dir, fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Bitmap getLocalBitmap(String url) {
		String finleName;
		try {
			finleName = MD5Encoder.encode(url);
			File cacheFile = new File(LOCAL_CACHE_PATH, finleName);
			
			if (cacheFile.exists()) {
				FileInputStream fileInputStream = new FileInputStream(cacheFile);
				Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
				return bitmap;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}

