package com.example.mobilesafe;

import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.ui.SettingView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * keep config of setting in sharedPreferences
 * @author Bei
 *
 */
public class SettingActivity extends Activity{
	private SharedPreferences sp;
	private SettingView sv_setting_update;
	private SettingView sv_setting_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		updata();
		
		sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
		
		address();
		
		
	}
	private void address() {
		sv_setting_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, AddressService.class);
				if (sv_setting_address.isChecked()) {
					startService(intent);
					sv_setting_address.setChecked(true);
				} else {
					stopService(intent);
					sv_setting_address.setChecked(false);
				}				
			}
		});
		
	}
	/**
	 * 选择是否联网检测更新
	 * @param sv_setting_update
	 */
	private void updata() {
		if (sp.getBoolean("update", true)){
			
//			sv_setting_update.setDes("开启更新");
			sv_setting_update.setChecked(true);
		} else {
//			sv_setting_update.setDes("关闭更新");
			sv_setting_update.setChecked(false);
		}
		
		sv_setting_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();
				if (sv_setting_update.isChecked()){
//					sv_setting_update.setDes("关闭更新");
					sv_setting_update.setChecked(false);
					edit.putBoolean("update", false);
				} else {
//					sv_setting_update.setDes("开启更新");
					sv_setting_update.setChecked(true);
					edit.putBoolean("update", true);
				}
//				edit.apply(); // 只能在level 9以后的版本使用
				edit.commit();
				
			}
		});
	}
}
