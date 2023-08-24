package com.example.myplaystore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myplaystore.domain.CategoryInfo;

public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return "category";
	}

	@Override
	public String getParims() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public ArrayList<CategoryInfo> parseData(String result) {
		
		try {
			JSONArray jsonArray = new JSONArray(result);
			ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();
			
			for(int i = 0; i < jsonArray.length(); i ++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				if (jsonObject.has("title")){
					CategoryInfo titleInfo = new CategoryInfo();
					titleInfo.title = jsonObject.getString("title");
					titleInfo.isTitle = true;
					list.add(titleInfo);
				}
				
				if (jsonObject.has("infos")) {
					JSONArray jsonInfos = jsonObject.getJSONArray("infos");
					
					for (int j = 0; j < jsonInfos.length(); j ++) {
						JSONObject jsonObject2 = jsonInfos.getJSONObject(j);
						CategoryInfo info = new CategoryInfo();
						info.name1 = jsonObject2.getString("name1");
						info.name2 = jsonObject2.getString("name2");
						info.name3 = jsonObject2.getString("name3");
						info.url1 = jsonObject2.getString("url1");
						info.url2 = jsonObject2.getString("url2");
						info.url3 = jsonObject2.getString("url3");
						info.isTitle = false;
						System.out.println("图片1的地址：" + info.url1);
						
						list.add(info);
					}
					
				}
			}
			
			return list;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}



}
