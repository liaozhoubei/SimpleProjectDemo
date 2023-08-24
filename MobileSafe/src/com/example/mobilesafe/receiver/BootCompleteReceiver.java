package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * SIM Listener and keep warning if SIM serialNumber changing
 * 
 * @author Bei
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("手机重启了，哈哈哈哈");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		if (sp.getBoolean("protected", false)) {
			String sim = sp.getString("sim", "");
			TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String simSerialNumber = tele.getSimSerialNumber();
			if (!TextUtils.isEmpty(sim) && !TextUtils.isEmpty(simSerialNumber)) {
				if (!sim.equals(simSerialNumber)) {
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(sp.getString("safeNum", "5556"), null, "我的电话卡被更改了", null, null);
				}
			}
		}

	}

}
