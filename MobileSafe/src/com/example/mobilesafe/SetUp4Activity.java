package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetUp4Activity extends SetUpBaseActivity{
	private CheckBox cb_setup4_protected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_setup4_protected = (CheckBox) findViewById(R.id.cb_setup4_protected);
		
		
		if (sp.getBoolean("protected", false)){
			cb_setup4_protected.setText("开启防盗保护");
			cb_setup4_protected.setChecked(true);
		} else {
			cb_setup4_protected.setText("关闭防盗保护");
			cb_setup4_protected.setChecked(false);
		}
		
		cb_setup4_protected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor edit = sp.edit();
				if (isChecked){
					cb_setup4_protected.setText("开启防盗保护");
					cb_setup4_protected.setChecked(true);
					edit.putBoolean("protected", true);
				} else {
					cb_setup4_protected.setText("关闭防盗保护");
					cb_setup4_protected.setChecked(false);
					edit.putBoolean("protected", false);
				}
				edit.commit();
			}
		});
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
