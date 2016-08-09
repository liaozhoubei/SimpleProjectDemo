package com.example.myplaystore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParims() {
		return "";
	}

	@Override
	public ArrayList<SubjectInfo> parseData(String result) {
		try {
			ArrayList<SubjectInfo> arrayList = new ArrayList<SubjectInfo>();
			JSONArray jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i ++){
				JSONObject obj = jsonArray.getJSONObject(i);
				SubjectInfo info = new SubjectInfo();
				info.des = obj.getString("des");
				info.url = obj.getString("url");
				arrayList.add(info);
			}
			return arrayList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
