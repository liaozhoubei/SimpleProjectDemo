package com.example.myplaystore.utils;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
	private static BitmapUtils bitmapUtils = null;
	
	public static BitmapUtils getBitmapUtils(){
		if (bitmapUtils == null) {
			synchronized(BitmapHelper.class) {
				if (bitmapUtils == null) {
					bitmapUtils = new BitmapUtils(UIUtils.getContext());
				}
			}
		}
		return bitmapUtils;
	}

}
