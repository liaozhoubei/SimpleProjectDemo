package com.example.mobilesafe.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSService extends Service {
	private MyLocation mMylocation;
	private LocationManager mLocationManager;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> providers = mLocationManager.getProviders(true);
		for (String str : providers) {
			System.out.println("可用的" + str);
		}
		// Criteria定位的属性
		Criteria criteria = new Criteria();
		criteria.setAltitudeRequired(true);
		String bestProvider = mLocationManager.getBestProvider(criteria, true);
		System.out.println(bestProvider);
		mMylocation = new MyLocation();
		mLocationManager.requestLocationUpdates(bestProvider, 0, 0, mMylocation);

	}

	private class MyLocation implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			double latitude = location.getLatitude();// 维度，平行
			double longitude = location.getLongitude();// 经度，
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(sp.getString("safeNum", "5556"), null, "latitude: " + latitude + "--- longitude" + longitude,
					null, null);
			stopSelf();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocationManager.removeUpdates(mMylocation);
	}
}
