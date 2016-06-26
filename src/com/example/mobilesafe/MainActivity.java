package com.example.mobilesafe;

import com.example.mobilesafe.adapter.GvAdapter;
import com.example.mobilesafe.utils.MD5Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GridView gv_main_gridview;
	private SharedPreferences sp;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		gv_main_gridview = (GridView) findViewById(R.id.gv_main_gridview);
		GvAdapter gvAdapter = new GvAdapter(MainActivity.this);
		gv_main_gridview.setAdapter(gvAdapter);
		gv_main_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					// set mobile password
					// if it has not password, set password.
					// else input password to enter setting mobile
					if (TextUtils.isEmpty(sp.getString("password", ""))) {
						// set password
						showSetPassWordDialog();
					} else {
						// enter password
						showEnterPasswordDialog();
					}
					break;
				case 8:
					Intent intent = new Intent(MainActivity.this, SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}

			}

			// show dialog to set password
			private void showSetPassWordDialog() {
				final AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setCancelable(false);
				View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
				final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
				final EditText et_setpassword_confrim = (EditText) view.findViewById(R.id.et_setpassword_confrim);
				Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
				Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);

				// click ok button
				btn_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String password = et_setpassword_password.getText().toString().trim();
						if (TextUtils.isEmpty(password)) {
							Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
							return;
						}
						String confirm_password = et_setpassword_confrim.getText().toString().trim();
						if (password.equals(confirm_password)) {
							// keep the password to sharedPerferences
							Editor edit = sp.edit();
							edit.putString("password", MD5Util.passwordMD5(password));
							edit.apply();
							Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "两次密码不相符", Toast.LENGTH_SHORT).show();
						}

					}
				});

				// click cancle button
				btn_cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				builder.setView(view);
				dialog = builder.create();
				dialog.show();

			}

			int count = 0;

			// show dialog to enter password
			private void showEnterPasswordDialog() {
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setCancelable(false);
				View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
				final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
				Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
				Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
				ImageView showPassword = (ImageView) view.findViewById(R.id.iv_enterpassword_hide);
				showPassword.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (count % 2 == 0) {
							// show the password
							// 0 is InputType for none
							et_setpassword_password.setInputType(0);
						} else {
							// hide the password
							// 129 is InputType for textPassword
							et_setpassword_password.setInputType(129);
						}
						count++;

					}
				});

				btn_ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String password = et_setpassword_password.getText().toString().trim();
						if (TextUtils.isEmpty(password)) {
							Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
							return;
						}
						if (MD5Util.passwordMD5(password).equals(sp.getString("password", ""))) {
							Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
							Intent intent = new Intent(MainActivity.this, SetUp1Activity.class);
							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
						}
					}
				});
				btn_cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
			}

		});
	}

}
