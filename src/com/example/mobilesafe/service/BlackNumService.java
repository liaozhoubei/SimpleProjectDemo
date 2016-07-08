package com.example.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.dao.BlackNumDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlackNumService extends Service {

	private SmsReceiver smsReceiver;
	private BlackNumDao blackNumDao;
	private TelephonyManager telephonyManager;
	private MyPhoneStateListener myPhoneStateListener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		blackNumDao = new BlackNumDao(getApplicationContext());
		smsReceiver = new SmsReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		// 最高拦截优先级Integer.MAX_VALUE
		intentFilter.setPriority(1000);
		registerReceiver(smsReceiver, intentFilter);
		
		myPhoneStateListener = new MyPhoneStateListener();
		
		telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	/**
	 * 监听电话状态，如果来电在黑名单则拦截
	 * @author ASUS-H61M
	 *
	 */
	public class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			if (state == TelephonyManager.CALL_STATE_RINGING){
				int mode = blackNumDao.queryBlackNumMode(incomingNumber);
				if (mode == BlackNumDao.CALL || mode == BlackNumDao.ALL){
					endCall();
					
					// 删除通话记录
					
					final ContentResolver resolver = getContentResolver();
					final Uri uri = Uri.parse("content://call_log/calls");
					resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
						@Override
						public void onChange(boolean selfChange) {
							// TODO Auto-generated method stub
							super.onChange(selfChange);
							resolver.delete(uri, "number=?", new String[]{incomingNumber});
						}
					});
				}
			}
		}

		/**
		 * 挂断电话
		 */
		private void endCall() {
			Class<?> loadClass;
			try {
				loadClass = BlackNumService.class.getClassLoader().loadClass("android.os.ServiceManager");
				Method method = loadClass.getDeclaredMethod("getService", String.class);
				IBinder invoke = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
				ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
				iTelephony.endCall();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private class SmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {

				SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
				String body = sms.getMessageBody();
				String sender = sms.getOriginatingAddress();
				System.out.println("我是通信卫士的拦截"+"姓名" + sender + "---- 内容" + body);
				int mode = blackNumDao.queryBlackNumMode(sender);
				if (mode == BlackNumDao.SMS || mode == BlackNumDao.ALL){
					abortBroadcast();
				}
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
	}

}
