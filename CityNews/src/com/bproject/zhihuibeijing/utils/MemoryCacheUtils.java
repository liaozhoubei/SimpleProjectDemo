package com.bproject.zhihuibeijing.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {
	// 不推荐的内存缓存加载
//	HashMap<String, Bitmap> mMemoryCache = new HashMap<String, Bitmap>();
	
	// 使用软引用
//	HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
	
	// 使用LruCache
	LruCache<String, Bitmap> cache;
	
	
	
	
	public MemoryCacheUtils() {
		// 获取App分配出来的内存大小
		long maxMemory = Runtime.getRuntime().maxMemory();
		cache = new LruCache<String, Bitmap>((int) maxMemory / 8){
			// 设置每个对象的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				// getByteCount()要在版本12中使用
//				int byteCount = value.getByteCount();
				// 源码方案
				int byteCount = value.getRowBytes() * value.getHeight();
				return byteCount;
			}
		};
	}

	public void setMemoryCache(String url, Bitmap bitmap){
//		mMemoryCache.put(url, bitmap);
		
		// 使用软引用
//		SoftReference<Bitmap> reference = new SoftReference<Bitmap>(bitmap);
//		mMemoryCache.put(url, reference);
		
		// 使用LruCache
		cache.put(url, bitmap);
		
	}
	
	public Bitmap getMemoryCache(String url){
//		SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//		if (softReference != null) {
//			Bitmap bitmap = softReference.get();
//			return bitmap;
//		}
//		return null;
		
		Bitmap bitmap = cache.get(url);
		return bitmap;
		
	}

}

