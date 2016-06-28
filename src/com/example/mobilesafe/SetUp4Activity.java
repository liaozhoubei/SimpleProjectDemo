package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class SetUp4Activity extends SetUpBaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
	}


	@Override
	public void pre_activity() {
		Intent intent = new Intent(SetUp4Activity.this, SetUp3Activity.class);
		startActivity(intent);
		finish();	
		overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
	}

	@Override
	public void next_activity() {
		Editor edit = sp.edit();
		edit.putBoolean("first", false);
		edit.commit();
		Intent intent = new Intent(SetUp4Activity.this, LostfindActivity.class);
		startActivity(intent);
		finish();			
	}
}
