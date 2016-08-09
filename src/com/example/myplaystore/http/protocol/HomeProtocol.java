package com.example.myplaystore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myplaystore.domain.AppInfo;

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParims() {
		return "";
	}

	@Override
	public ArrayList<AppInfo> parseData(String result) {

		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject1 = jsonArray.getJSONObject(i);
				AppInfo info = new AppInfo();
				//
				info.des = jsonObject1.getString("des");
				info.downloadUrl = jsonObject1.getString("downloadUrl");
				info.iconUrl = jsonObject1.getString("iconUrl");
				info.id = jsonObject1.getString("id");
				info.name = jsonObject1.getString("name");
				info.packageName = jsonObject1.getString("packageName");
				info.size = jsonObject1.getLong("size");
				info.stars = (float) jsonObject1.getDouble("stars");
				appInfos.add(info);
			}

			// 初始化轮播条的数据
			JSONArray ja1 = jsonObject.getJSONArray("picture");
			ArrayList<String> pictures = new ArrayList<String>();
			for (int i = 0; i < ja1.length(); i++) {
				String pic = ja1.getString(i);
				pictures.add(pic);
			}
			
			return appInfos;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
