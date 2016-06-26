package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp2Activity extends SetUpBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}


	@Override
	public void pre_activity() {
		Intent intent = new Intent(SetUp2Activity.this, SetUp1Activity.class);
		startActivity(intent);
		finish();		
	}

	@Override
	public void next_activity() {
		Intent intent = new Intent(SetUp2Activity.this, SetUp3Activity.class);
		startActivity(intent);
		finish();		
	}
}
