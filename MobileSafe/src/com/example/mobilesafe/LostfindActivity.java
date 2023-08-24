package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
			TextView tv_lostfind_safenum = (TextView) findViewById(R.id.tv_lostfind_safenum);
			ImageView tv_lostfind_protected = (ImageView) findViewById(R.id.tv_lostfind_protected);
			tv_lostfind_safenum.setText(sp.getString("safeNum", "5556"));
			boolean b = sp.getBoolean("protected", false);
			if (b) {
				tv_lostfind_protected.setImageResource(R.drawable.lock);
			} else {
				tv_lostfind_protected.setImageResource(R.drawable.unlock);
			}
			
		}
	}
	
	public void resetup(View v) {
		Intent intent = new Intent(LostfindActivity.this, SetUp1Activity.class);
		startActivity(intent);
		finish();
	}
}
