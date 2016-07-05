package com.example.mobilesafe.service;

import com.example.mobilesafe.dao.AddressDao;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 监听电话，展示归属地
 * @author ASUS-H61M
 *
 */
public class AddressService extends Service {

	private PhoneListener listener;
	private TelephonyManager telephonyManager;
	private WindowManager windowManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new PhoneListener();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public class PhoneListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// 电话铃声响起的时候
				String queryAddress = AddressDao.queryAddress(incomingNumber, getApplicationContext());
				if (!TextUtils.isEmpty(queryAddress)) {
//					Toast.makeText(getApplicationContext(), queryAddress, Toast.LENGTH_LONG).show();
					showToast(queryAddress);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// 电话铃声挂断的时候
				hideToast();
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				// 处于空闲状态
				break;

			default:
				break;
			}
		}




	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
	
	// show custom Toast
	private void showToast(String queryAddress) {
		TextView textview = new TextView(getApplicationContext());
		textview.setText(queryAddress);
		textview.setTextSize(20);
		textview.setTextColor(Color.BLUE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		
		windowManager.addView(textview, params);
		
		
	}
	
	private void hideToast() {
//		if (windowManager != null && )
	}

}
	

