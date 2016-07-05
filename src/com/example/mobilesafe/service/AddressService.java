package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.AddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
	private SharedPreferences sp;
	private PhoneListener listener;
	private TelephonyManager telephonyManager;
	private WindowManager windowManager;
	private View mView;
	private MyOutGoingCallReceiver mMyOutGoingCallReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new PhoneListener();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		mMyOutGoingCallReceiver = new MyOutGoingCallReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(mMyOutGoingCallReceiver, intentFilter);
		
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
				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				// 处于空闲状态
				hideToast();
				break;

			default:
				break;
			}
		}
	}

	
	// show custom Toast
	private void showToast(String queryAddress) {
		int[] bgcolor = new int[] { 
				R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		mView = View.inflate(getApplicationContext(), R.layout.toast_custom, null);
		TextView tv_toastcustom_address = (TextView) mView.findViewById(R.id.tv_toastcustom_address);
		tv_toastcustom_address.setText(queryAddress);
		mView.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		
		windowManager.addView(mView, params);
		
		
	}
	/**
	 * hide custom Toast
	 */
	private void hideToast() {
		if (windowManager != null && mView != null) {
			windowManager.removeView(mView);
			windowManager = null;
			mView = null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(mMyOutGoingCallReceiver);
	}
	/**
	 * 外拨电话时的广播接收者
	 * @author Bei
	 *
	 */
	private class MyOutGoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 查询外拨电话时的归属地
			String resultData = getResultData();
			String queryAddress = AddressDao.queryAddress(resultData, context);
			if (!TextUtils.isEmpty(queryAddress)){
				showToast(queryAddress);
			}
			
			
		}
		
		
	}

}
	

