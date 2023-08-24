package com.example.mobilesafe;



import com.example.mobilesafe.engine.SMSEngine;
import com.example.mobilesafe.engine.SMSEngine.ShowProgress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends Activity {
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	public void queryaddress(View v){
		//跳转到查询号码归属地页面
		Intent intent = new Intent(this,AddressActivity.class);
		startActivity(intent);
	}
	//备份短信
	public void backupsms(View v) {
		progressDialog = new ProgressDialog(AToolsActivity.this);
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
		new Thread(){
			public void run() {
				SMSEngine.getAllSMS(getApplicationContext(), new ShowProgress() {
					
					@Override
					public void setProgress(int progress) {
						progressDialog.setProgress(progress);						
					}
					
					@Override
					public void setMax(int max) {
						progressDialog.setMax(max);
					}
				});
				progressDialog.dismiss();
			};
			
		}.start();
		
	}
	// 恢复短信
	public void restoresms(View v) {
		// 需要解析备份的XML，但这里以插入短信演示
		ContentResolver contentResolver = getContentResolver(); 
		Uri uri = Uri.parse("content://sms");
		ContentValues values = new ContentValues();
		values.put("address", "123456");
		values.put("date", System.currentTimeMillis());
		values.put("type", 1);
		values.put("body", "哈哈哈，我来了");
		contentResolver.insert(uri, values);
	}
}
