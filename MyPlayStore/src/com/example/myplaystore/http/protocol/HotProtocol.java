package com.example.myplaystore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myplaystore.domain.AppInfo;

public class HotProtocol extends BaseProtocol<ArrayList<String>> {

	@Override
	public String getKey() {
		return "hot";
	}

	@Override
	public String getParims() {
		return "";
	}

	@Override
	public ArrayList<String> parseData(String result) {
		
		try {
			JSONArray array = new JSONArray(result);
			ArrayList<String> arrayList = new ArrayList<String>();
			for(int i = 0; i < array.length(); i ++) {
				String string = array.getString(i);
				arrayList.add(string);
			}
			return arrayList;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

}
