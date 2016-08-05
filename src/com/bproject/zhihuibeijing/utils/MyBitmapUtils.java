package com.bproject.zhihuibeijing.utils;

import com.bproject.zhihuibeijing.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils {
	private NetCacheUtils mNetCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;

	public MyBitmapUtils() {
		this.mLocalCacheUtils = new LocalCacheUtils();
		this.mMemoryCacheUtils = new MemoryCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
	}
	
	public void display(ImageView imageView, String url) {
		imageView.setImageResource(R.drawable.pic_item_list_default);
		Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			System.out.println("从内存加载图片啦");
			return;
		}
		
		
		bitmap = mLocalCacheUtils.getLocalBitmap(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			// 将图片写入内存
			mMemoryCacheUtils.setMemoryCache(url, bitmap);
			System.out.println("从本地加载图片啦");
			return;
		}
		
		mNetCacheUtils.getBitmapFromNet(imageView, url);
	}
	
}

