package com.example.mobilesafe;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends Activity {
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
}
