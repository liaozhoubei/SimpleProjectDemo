package com.example.mobilesafe;

import com.example.mobilesafe.ui.SettingView;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SetUp2Activity extends SetUpBaseActivity{
	private SettingView  sv_setup2_sim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sv_setup2_sim = (SettingView) findViewById(R.id.sv_setup2_sim);
		if (TextUtils.isEmpty(sp.getString("sim", ""))) {
			sv_setup2_sim.setChecked(false);
		} else {
			sv_setup2_sim.setChecked(true);
		}
		sv_setup2_sim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();
				if (sv_setup2_sim.isChecked()) {
					edit.putString("sim", "");
					sv_setup2_sim.setChecked(false);
				} else{
					// get the SIM number;
					TelephonyManager tele = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//					tele.getLine1Number();// it is not use in china
					String sim = tele.getSimSerialNumber(); // need user permission
					edit.putString("sim", sim);
					sv_setup2_sim.setChecked(true);
				}
				edit.commit();
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
		if (sv_setup2_sim.isChecked()){
			Intent intent = new Intent(SetUp2Activity.this, SetUp3Activity.class);
			startActivity(intent);
			finish();	
			overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
		} else {
			Toast.makeText(getApplicationContext(), "请绑定SIM卡", Toast.LENGTH_SHORT).show();
		}
		
	}
}
