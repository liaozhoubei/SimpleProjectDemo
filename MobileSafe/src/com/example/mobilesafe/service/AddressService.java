package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.AddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
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
	private WindowManager.LayoutParams mParams;
	private int mHeightPixels;
	private int mWidthPixels;

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
		
		// 获取屏幕宽 、高
		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics );
		mWidthPixels = outMetrics.widthPixels;
		mHeightPixels = outMetrics.heightPixels;
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

	
	// show custom Toast when call on
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
		
		mParams = new WindowManager.LayoutParams();
		
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                ;
        
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = sp.getInt("X", 100);
        mParams.y = sp.getInt("Y", 100);
        
        setTouch();
		
		windowManager.addView(mView, mParams);
		
		
	}
	private void setTouch() {
		mView.setOnTouchListener(new OnTouchListener() {
			
			private int mStartX;
			private int mStartY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 获得手指位置初始值
					mStartX = (int) event.getRawX();
					mStartY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 手指移动的时候
					// 获得手指移动时的坐标
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					
					// 计算手指移动的距离
					int dx = newX - mStartX;
					int dy = newY - mStartY;
					
					mParams.x += dx;
					mParams.y += dy;
					
					if (mParams.x < 0) {
						mParams.x = 0;
					}
					if (mParams.y < 0){
						mParams.y = 0;
					}
					if (mParams.x > mWidthPixels - mView.getWidth()){
						mParams.x = mWidthPixels - mView.getWidth();
					}
					if (mParams.y > mHeightPixels - mView.getHeight()) {
						mParams.y = mHeightPixels - mView.getHeight();
					}
					

					
					windowManager.updateViewLayout(mView, mParams);
					
					mStartX = newX;
					mStartY = newY;
					break;
				case MotionEvent.ACTION_UP:
					// 手指抬起的时候
					Editor edit = sp.edit();
					int X = mParams.x;
					int Y = mParams.y;
					edit.putInt("X", X);
					edit.putInt("Y", Y);
					edit.commit();
					break;
				}
				
				return true;
			}
		});
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
	

