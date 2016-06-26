package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LostfindActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("first", true)){
			Intent intent = new Intent(LostfindActivity.this, SetUp1Activity.class);
			startActivity(intent);
			finish();
		} else {
			setContentView(R.layout.activity_lostfind);
		}
	}
}
