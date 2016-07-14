package com.example.mobilesafe.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Xml;
/**
 * 获取手机中的短信内容
 * @author ASUS-H61M
 *
 */
public class SMSEngine {
	
	
	public interface ShowProgress{
		
		public void setMax(int max);
		
		public void setProgress(int progress);
	}
	
	
	
	public static void getAllSMS(Context context, ShowProgress showProgress) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, new String[] { "address", "date", "type", "body" }, null, null, null);
		int count = query.getCount();
		showProgress.setMax(count);
		int progress = 0;
		// 备份短信到XML
		XmlSerializer xmlSerializer = Xml.newSerializer();
		// 设置XML保存路径
		try {
			xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
			xmlSerializer.startDocument("utf-8", true);
			xmlSerializer.startTag(null, "smss");
			while (query.moveToNext()) {
				SystemClock.sleep(1000);
				xmlSerializer.startTag(null, "sms");
				
				xmlSerializer.startTag(null, "address");
				String address = query.getString(0);
				xmlSerializer.text(address);
				xmlSerializer.endTag(null, "address");
				
				xmlSerializer.startTag(null, "date");
				String date = query.getString(1);
				xmlSerializer.text(date);
				xmlSerializer.endTag(null, "date");
				
				xmlSerializer.startTag(null, "type");
				String type = query.getString(2);
				xmlSerializer.text(type);
				xmlSerializer.endTag(null, "type");
				
				xmlSerializer.startTag(null, "body");
				String body = query.getString(3);
				xmlSerializer.text(body);
				xmlSerializer.endTag(null, "body");
				System.out.println("addredd:" + address + "   date:" + date + "   type:" + type + "    body:" + body);
				
				xmlSerializer.endTag(null, "sms");
				
				progress++;
				showProgress.setProgress(progress);
			}
			
			xmlSerializer.endTag(null, "smss");
			xmlSerializer.endDocument();
			xmlSerializer.flush();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
