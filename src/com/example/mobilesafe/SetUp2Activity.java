package com.example.mobilesafe;

import com.example.mobilesafe.ui.SettingView;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class SetUp2Activity extends SetUpBaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		final SettingView  sv_setting_update= (SettingView) findViewById(R.id.sv_setting_update);
		if (TextUtils.isEmpty(sp.getString("sim", ""))){
			sv_setting_update.setChecked(false);
		} else {
			sv_setting_update.setChecked(true);
		}
		sv_setting_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (sv_setting_update.isChecked()) {
					Editor edit = sp.edit();
					edit.putString("sim", "");
					edit.commit();
					sv_setting_update.setChecked(false);
				} else{
					// get the SIM number;
					TelephonyManager tele = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					tele.getLine1Number();// it is not use in china
					String sim = tele.getSimSerialNumber(); // need user permission
					Editor edit = sp.edit();
					edit.putString("sim", sim);
					edit.commit();
					sv_setting_update.setChecked(true);
				}
					
			}
		});
	}


	@Override
	public void pre_activity() {
		Intent intent = new Intent(SetUp2Activity.this, SetUp1Activity.class);
		startActivity(intent);
		finish();		
		overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
	}

	@Override
	public void next_activity() {
		Intent intent = new Intent(SetUp2Activity.this, SetUp3Activity.class);
		startActivity(intent);
		finish();	
		overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
	}
}
