package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.GPSService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
/*
 * By receiving an SMS to treat the phone(get location of phone, wipedata of phone``)
 */
public class SmsReceiver extends BroadcastReceiver {

	private static MediaPlayer sMediaPlayer;
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(context, Admin.class);

		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {

			SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
			String body = sms.getMessageBody();
			String sender = sms.getOriginatingAddress();
			System.out.println("姓名" + sender + "---- 内容" + body);
			abortBroadcast();
			if ("#*location*#".equals(body)) {
				// GPS追踪
				Intent GPSintent = new Intent(context, GPSService.class);
				context.startService(GPSintent);
				System.out.println("GPS追踪");
				// 拦截短信,在4.4及以上版本已经无法拦截短信
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			} else if ("#*alarm*#".equals(body)) {
				AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamVolume(AudioManager.STREAM_MUSIC), 0);
				if (sMediaPlayer != null) {
					sMediaPlayer.release();
				}
				sMediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
				// it is not useful always!
//				mediaPlayer.setVolume(1.0f, 1.0f);
//				mediaPlayer.setLooping(true);
				sMediaPlayer.start();
				System.out.println("播放报警音乐");
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			} else if ("#*wipedata*#".equals(body)) {
				// 远程删除数据
				System.out.println("远程删除数据");
				if (mDevicePolicyManager.isAdminActive(mComponentName)){
					mDevicePolicyManager.wipeData(0);
				}
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			} else if ("#*lockscreen*#".equals(body)) {
				// 远程锁屏
				System.out.println("远程锁屏");
				if (mDevicePolicyManager.isAdminActive(mComponentName)) {
					mDevicePolicyManager.lockNow();
				}
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			}
		}

	}

}
