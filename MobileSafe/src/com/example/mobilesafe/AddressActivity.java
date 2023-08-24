package com.example.mobilesafe;


import com.example.mobilesafe.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressActivity extends Activity{
	private EditText et_address_queryphone;
	private TextView tv_address_queryaddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		et_address_queryphone = (EditText) findViewById(R.id.et_address_queryphone);
		tv_address_queryaddress = (TextView) findViewById(R.id.tv_address_queryaddress);
		
		et_address_queryphone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String string = s.toString();
				if (!TextUtils.isEmpty(string)){
					String queryAddress = AddressDao.queryAddress(string, getApplicationContext());
					tv_address_queryaddress.setText(queryAddress);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 查询号码归属地操作
	 * @param v
	 */
	public void query(View v){
		//1.获取输入的号码
		String phone = et_address_queryphone.getText().toString().trim();
		//2.判断号码是否为空
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(getApplicationContext(), "请输入要查询号码", 0).show();
			// 增加输入框摇晃的效果
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_address_queryphone.startAnimation(shake);
			// 增加震动的效果
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(100);// 震动100毫秒
			return;
		}
		//3.根据号码查询号码归属地
		String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
		//4.判断查询的号码归属地是否为空
		if (!TextUtils.isEmpty(queryAddress)) {
			tv_address_queryaddress.setText(queryAddress);
		}
	}
}
