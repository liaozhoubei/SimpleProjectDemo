package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public  class ContactEngine {
	
	public static List<HashMap<String, String>> getAllContactInfo(Context context) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		ContentResolver resolver = context.getContentResolver();
		Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri data_uri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
		while(cursor.moveToNext()){
			String contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
			Cursor cursorInfo = resolver.query(data_uri, new String[]{"data1", "mimitype"}, "contact_id=?", new String[]{contact_id}, null);
			HashMap<String, String> map = new HashMap<String, String>();
			while(cursorInfo.moveToNext()){
				String data1 = cursorInfo.getString(0);
				String mimetype = cursorInfo.getString(1);
				
				
				if (mimetype.equals("vnd.android.cursor.item/name")){
					map.put("name", data1);
				}else if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
					map.put("phone", data1);
				}
			}
			list.add(map);
			cursorInfo.close();
			
		}
		cursor.close();
		return list;
		
		
	}
}
