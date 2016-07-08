package com.example.mobilesafe;

import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.BlackNumService;
import com.example.mobilesafe.ui.SettingClickView;
import com.example.mobilesafe.ui.SettingView;
import com.example.mobilesafe.utils.AddressUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
	private SettingClickView scv_setting_changedbg;
	private SettingClickView scv_setting_location;
	private SettingView sv_setting_blacknum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
		scv_setting_changedbg = (SettingClickView) findViewById(R.id.scv_setting_changedbg);
		scv_setting_location = (SettingClickView) findViewById(R.id.scv_setting_location);
		sv_setting_blacknum = (SettingView) findViewById(R.id.sv_setting_blacknum);
		
		updata();
		changedbg();
		location();
		
		
	}
	/**
	 * 更改号码归属地视图出现的位置
	 */
	private void location() {
		scv_setting_location.setTitle("归属地提示框位置");
		scv_setting_location.setDes("设置归属地提示框的显示位置");
		scv_setting_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到设置位置的界面
				Intent intent = new Intent(SettingActivity.this,DragViewActivity.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * 设置号码归属地视图背景
	 */
	private void changedbg() {
		final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		//设置标题和描述信息
		scv_setting_changedbg.setTitle("归属地提示框风格");
		//根据保存的选中的选项的索引值设置自定义组合控件描述信息回显操作
		scv_setting_changedbg.setDes(items[sp.getInt("which", 0)]);
		scv_setting_changedbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new Builder(SettingActivity.this);
				dialog.setIcon(R.drawable.ic_launcher);
				dialog.setTitle("归属地提示框风格");
				dialog.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor edit = sp.edit();
						edit.putInt("which", which);
						edit.commit();
						String string = items[which];
						scv_setting_changedbg.setDes(string);
						dialog.dismiss();
					}
				});
				// null代表隐藏对话框
				dialog.setNegativeButton("cancle", null);
				dialog.show();
			}
			
		});
		
		
	}
	// 当Activity可见的时候执行
	@Override
	protected void onStart() {
		// 当界面在后天运行，但是用户直接点击stopServer服务时的回显
		address();
		blackNum();
		super.onStart();
	}
	// 黑名单拦截操作
	private void blackNum() {
		if (AddressUtil.isRunningServer("com.example.mobilesafe.service.BlackNumService", getApplicationContext())){
			sv_setting_blacknum.setChecked(true);
		} else {
			sv_setting_blacknum.setChecked(false);
		}
		sv_setting_blacknum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, BlackNumService.class);
				if (sv_setting_blacknum.isChecked()) {
					stopService(intent);
					sv_setting_blacknum.setChecked(false);
				} else {
					startService(intent);
					sv_setting_blacknum.setChecked(true);
				}				
			}
		});
				
	}
	private void address() {
		if (AddressUtil.isRunningServer("com.example.mobilesafe.service.AddressService", getApplicationContext())){
			sv_setting_address.setChecked(true);
		} else {
			sv_setting_address.setChecked(false);
		}
		
		sv_setting_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, AddressService.class);
				if (sv_setting_address.isChecked()) {
					stopService(intent);
					sv_setting_address.setChecked(false);
				} else {
					startService(intent);
					sv_setting_address.setChecked(true);
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
