package com.bproject.zhihuibeijing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {
	
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	
	public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}

	public void getBitmapFromNet(ImageView imageView, String url){
		new BitmapTask().execute(imageView, url);
	}
	
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{

		private ImageView imageView;
		private String url;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			url = (String) params[1];
			imageView.setTag(url);
			
			Bitmap bitmap = DownloadBitmap(url);
			
			return bitmap;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				String url = (String) imageView.getTag();
				
				if (url.equals(this.url)){
					imageView.setImageBitmap(result);
					mLocalCacheUtils.setLocalCache(url, result);
					mMemoryCacheUtils.setMemoryCache(url, result);
					
				}
				
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	public Bitmap DownloadBitmap(String url){
		HttpURLConnection conn = null;
		try {
			URL Url = new URL(url);
			
			conn = (HttpURLConnection) Url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(50* 1000);
			conn.setReadTimeout(5 * 1000);
			
			conn.connect();
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream inputStream = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

}
