package com.example.mobilesafe.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	public static String queryAddress(String num, Context context) {
		String location = "";
		File file = new File(context.getFilesDir(), "address.db");
		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if (num.matches("^1[34578]\\d{9}$")) {
//			Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where area=?", new String[]{num.substring(0, 7)});
			Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{num.substring(0, 7)});
			if (cursor.moveToNext()) {
				location = cursor.getString(0);
				System.out.println(location + "很奇怪");
			}	
			cursor.close();
		} else{
			switch (num.length()) {
			case 3://110  120  119  911
				location = "特殊电话";
				break;
			case 4://5554   5556
				location = "虚拟电话";
				break;
			case 5://10086  10010  10000
				location ="客服电话";
				break;
			case 7://座机,本地电话
			case 8:
				location="本地电话";
				break;

			default:// 010 1234567   10位  	010 12345678  11位     0372  12345678  12位
				//长途电话
				if (num.length() >= 10  &&  num.startsWith("0")) {
					//根据区号查询号码归属
					//1.获取号码的区号
					//3位,4位
					//3位
					String result = num.substring(1, 3);//010   10
					//2.根据区号查询号码归属地
					Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where area=?", new String[]{result});
					//3.解析cursor
					if (cursor.moveToNext()) {
						location = cursor.getString(0);
						//截取数据
						location = location.substring(0, location.length()-2);
						cursor.close();
					}else{
						//3位没有查询到,直接查询4位
						//获取4位的区号
						result = num.substring(1, 4);//0372    372
						cursor = sqLiteDatabase.rawQuery("select location from data2 where area=?", new String[]{result});
						if (cursor.moveToNext()) {
							location = cursor.getString(0);
							location = location.substring(0, location.length()-2);
							cursor.close();
						}
					}
				}
				break;
			}
		}
		
		sqLiteDatabase.close();
		return location;

	}
}
