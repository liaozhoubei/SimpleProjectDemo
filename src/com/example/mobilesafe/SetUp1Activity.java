package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp1Activity extends SetUpBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	

	@Override
	public void pre_activity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next_activity() {
		Intent intent = new Intent(SetUp1Activity.this, SetUp2Activity.class);
		startActivity(intent);
		finish();		
	}

}
