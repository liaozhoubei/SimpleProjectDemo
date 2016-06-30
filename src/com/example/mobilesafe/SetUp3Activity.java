package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetUp3Activity extends SetUpBaseActivity{
	private EditText et_setup3_safenum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_safenum = (EditText) findViewById(R.id.et_setup3_safenum);
		et_setup3_safenum.setText(sp.getString("safenum", ""));
	}

	@Override
	public void pre_activity() {
		Intent intent = new Intent(SetUp3Activity.this, SetUp2Activity.class);
		startActivity(intent);
		finish();		
		overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
	}
	@Override
	public void next_activity() {
		String safenum = et_setup3_safenum.getText().toString().trim();
		if (TextUtils.isEmpty(safenum)) {
			Toast.makeText(getApplicationContext(), "请输入安全号码", Toast.LENGTH_SHORT).show();
			return;
		} 
		Editor edit = sp.edit();
		edit.putString("safenum", safenum);
		edit.commit();
		Intent intent = new Intent(SetUp3Activity.this, SetUp4Activity.class);
		startActivity(intent);
		finish();	
		overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
	}
	
	
	public void selectContacts(View v) {
		
	}

}
