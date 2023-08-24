package com.example.myplaystore.http.protocol;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.AppInfo.SafeInfo;

public class HomeDetailProtocol extends BaseProtocol<AppInfo> {
	private String packageName;
	
	
	
	public HomeDetailProtocol(String packageName) {
		super();
		this.packageName = packageName;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return "detail";
	}

	@Override
	public String getParims() {
		// TODO Auto-generated method stub
		return "&packageName=" + packageName;
	}

	@Override
	public AppInfo parseData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			
			AppInfo appInfo = new AppInfo();
			
			appInfo.author = jsonObject.getString("author");
			appInfo.date = jsonObject.getString("date");
			appInfo.des = jsonObject.getString("des");
			appInfo.downloadNum = jsonObject.getString("downloadNum");
			appInfo.downloadUrl = jsonObject.getString("downloadUrl");
			appInfo.iconUrl = jsonObject.getString("iconUrl");
			appInfo.id = jsonObject.getString("id");
			appInfo.name = jsonObject.getString("name");
			appInfo.size = jsonObject.getLong("size");
			appInfo.packageName = jsonObject.getString("packageName");
			appInfo.stars = (float) jsonObject.getDouble("stars");
			appInfo.version = jsonObject.getString("version");
			
			// 解析安全中的数据
			ArrayList<SafeInfo> safeInfos = new ArrayList<AppInfo.SafeInfo>();
			JSONArray safeArray = jsonObject.getJSONArray("safe");
			for (int i = 0; i < safeArray.length(); i ++) {
				JSONObject safeObject = safeArray.getJSONObject(i);
				SafeInfo safeInfo = new SafeInfo();
				safeInfo.safeDes = safeObject.getString("safeDes");
				safeInfo.safeDesUrl = safeObject.getString("safeDesUrl");
				safeInfo.safeUrl = safeObject.getString("safeUrl");
				
				safeInfos.add(safeInfo);
			}
			appInfo.safe = safeInfos;
			// 解析图片中的数据
			ArrayList<String> screen = new ArrayList<String>();
			JSONArray screenArray = jsonObject.getJSONArray("screen");
			for (int i = 0; i < screenArray.length(); i++) {
				String screenUrl = screenArray.getString(i);
				screen.add(screenUrl);
			}
			appInfo.screen = screen;
			
			return appInfo;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


}
