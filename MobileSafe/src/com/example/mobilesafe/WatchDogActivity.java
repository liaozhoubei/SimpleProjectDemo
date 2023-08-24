package com.example.mobilesafe;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WatchDogActivity extends Activity {

	private ImageView iv_watchdog_icon;
	private TextView tv_watchdog_name;
	private EditText ed_watchdog_password;
	private String packageName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchdog);
		iv_watchdog_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
		tv_watchdog_name = (TextView) findViewById(R.id.tv_watchdog_name);
		ed_watchdog_password = (EditText) findViewById(R.id.ed_watchdog_password);
		
		Intent intent = getIntent();
		packageName = intent.getStringExtra("packageName");
		PackageManager packageManager = getPackageManager();
		try {
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
			Drawable appIcon = applicationInfo.loadIcon(packageManager);
			String appName = applicationInfo.loadLabel(packageManager).toString();
			
			iv_watchdog_icon.setImageDrawable(appIcon);
			tv_watchdog_name.setText(appName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
	
	// 解锁按键
	public void lock(View v ) {
		String password = ed_watchdog_password.getText().toString().trim();
		if ("123".equals(password)){
			Intent intent = new Intent("com.example.mobilesafe.watchdogactivity.unlock");
			intent.putExtra("packageName", packageName);
			sendBroadcast(intent);
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
		}
	}
	

	
}
