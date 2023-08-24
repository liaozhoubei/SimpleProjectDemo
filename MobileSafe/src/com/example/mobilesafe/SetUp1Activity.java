package com.example.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class SetUp1Activity extends SetUpBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	

	@Override
	public void pre_activity() {
		Intent intent = new Intent(SetUp1Activity.this, MainActivity.class);
		startActivity(intent);
		finish();	
		overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);		
	}

	@Override
	public void next_activity() {
		Intent intent = new Intent(SetUp1Activity.this, SetUp2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
	}

}
