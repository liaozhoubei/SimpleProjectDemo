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

//public class LocalCacheUtils {
//
//	private static final String LOCAL_CACHE_PATH = Environment
//			.getExternalStorageDirectory().getAbsolutePath() + "/zhbj74_cache";
//
//	// 写本地缓存
//	public void setLocalCache(String url, Bitmap bitmap) {
//		File dir = new File(LOCAL_CACHE_PATH);
//		if (!dir.exists() || !dir.isDirectory()) {
//			dir.mkdirs();// 创建文件夹
//		}
//
//		try {
//			String fileName = MD5Encoder.encode(url);
//
//			File cacheFile = new File(dir, fileName);
//
//			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
//					cacheFile));// 参1:图片格式;参2:压缩比例0-100; 参3:输出流
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 读本地缓存
//	public Bitmap getLocalCache(String url) {
//		try {
//			File cacheFile = new File(LOCAL_CACHE_PATH, MD5Encoder.encode(url));
//
//			if (cacheFile.exists()) {
//				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
//						cacheFile));
//				return bitmap;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//}