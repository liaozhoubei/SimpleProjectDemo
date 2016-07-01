package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
/*
 * By receiving an SMS to treat the phone(get location of phone, wipedata of phone``)
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {

			SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
			String body = sms.getMessageBody();
			String sender = sms.getOriginatingAddress();
			System.out.println("姓名" + sender + "---- 内容" + body);
			abortBroadcast();
			if ("#*location*#".equals(body)) {
				// GPS追踪
				System.out.println("GPS追踪");
				// 拦截短信,在4.4及以上版本已经无法拦截短信
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			} else if ("#*alarm*#".equals(body)) {
				// 播放报警音乐
				System.out.println("播放报警音乐");
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			} else if ("#*wipedata*#".equals(body)) {
				// 远程删除数据
				System.out.println("远程删除数据");
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			} else if ("#*lockscreen*#".equals(body)) {
				// 远程锁屏
				System.out.println("远程锁屏");
				abortBroadcast();// 拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
			}
		}

	}

}
