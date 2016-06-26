package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
	}

	@Override
	public void next_activity() {
		// TODO Auto-generated method stub
		
	}
}
