package com.example.mobilesafe;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
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

		et_setup3_safenum = (EditText)findViewById(R.id.et_setup3_safenum);
		et_setup3_safenum.setText(sp.getString("safeNum", ""));

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
		String safeNum = et_setup3_safenum.getText().toString().trim();
		if (!TextUtils.isEmpty(safeNum)){
			Editor edit = sp.edit();
			edit.putString("safeNum", safeNum);
			edit.commit();
			Intent intent = new Intent(SetUp3Activity.this, SetUp4Activity.class);
			startActivity(intent);
			finish();	
			overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
		} else {
			Toast.makeText(getApplicationContext(), "请设置安全号码", Toast.LENGTH_SHORT).show();
		}
		
	}
	// button of get contacts
	public void selectContacts(View v) {
//		Intent intent = new Intent(SetUp3Activity.this, ContactActivity.class);
//		startActivityForResult(intent, 0);
		
		// get Contact from default Contacts
		Intent intent = new Intent();
		intent.setAction("android.intent.action.PICK");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("vnd.android.cursor.dir/phone_v2");
		startActivityForResult(intent, 1);
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// get Contact from custom Activity
//		if (data != null) {
//			String safeNum = data.getStringExtra("phone");
//			et_setup3_safenum.setText(safeNum);
//		}
		
		// get Contact from default Contacts
		if (data != null) {
			Uri uri = data.getData();
			String num = null;
			ContentResolver resolver = getContentResolver();
			Cursor cursor = resolver.query(uri, null, null, null, null);
			while (cursor.moveToNext()){
				num = cursor.getString(cursor.getColumnIndex("data1"));
			}
			cursor.close();
			num = num.replace("-", "");
			et_setup3_safenum.setText(num);
		}
		
	
	}

}
