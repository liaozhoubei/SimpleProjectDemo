package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp3Activity extends SetUpBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}

	@Override
	public void pre_activity() {
		Intent intent = new Intent(SetUp3Activity.this, SetUp2Activity.class);
		startActivity(intent);
		finish();		
	}
	@Override
	public void next_activity() {
		Intent intent = new Intent(SetUp3Activity.this, SetUp4Activity.class);
		startActivity(intent);
		finish();		
	}

}
