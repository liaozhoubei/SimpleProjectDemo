package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.engine.AppEngine;
import com.example.mobilesafe.utils.MyAsycnTaks;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SoftMangaerActivity extends Activity {
	private ListView mLv_softmanager_application;
	private ProgressBar mLoading;
	private List<AppInfo> appInfos;
	private List<AppInfo> userInofs;
	private List<AppInfo> systemInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmanager);
		mLv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
		mLoading = (ProgressBar) findViewById(R.id.loading);
		fillData();
	}

	private void fillData() {
		new MyAsycnTaks() {
			
			@Override
			public void preTask() {
				mLoading.setVisibility(View.VISIBLE);				
			}
			
			@Override
			public void postTast() {
				mLv_softmanager_application.setAdapter(new AppInfoAdapter());
				mLoading.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void doingTast() {
				appInfos = AppEngine.getAppInfos(getApplicationContext());
				userInofs = new ArrayList<AppInfo>();
				systemInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUser()){
						userInofs.add(appInfo);
					} else {
						systemInfos.add(appInfo);
					}
				}

			}
		}.excute();
	}
	
	private class AppInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return userInofs.size() + systemInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return appInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			View view;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_itemsoftmanage_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanage_icon);
				viewHolder.tv_softmanager_name = (TextView) view.findViewById(R.id.tv_softmanager_name);
				viewHolder.tv_softmanager_issd = (TextView) view.findViewById(R.id.tv_softmanager_issd);
				viewHolder.tv_softmanager_version = (TextView) view.findViewById(R.id.tv_softmanager_version);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			AppInfo info = appInfos.get(position);
			
			
			if (position == 0 ) {
				TextView text = new TextView(getApplicationContext());
				text.setText("用户程序");
				text.setBackgroundColor(Color.GRAY);
				text.setTextColor(Color.WHITE);
				return text;
			} else if ( position == (userInofs.size() + 1)) {
				TextView text = new TextView(getApplicationContext());
				text.setText("系统程序");
				text.setBackgroundColor(Color.GRAY);
				text.setTextColor(Color.WHITE);
				return text;
			}
			
			viewHolder.iv_itemsoftmanage_icon.setImageDrawable(info.getIcon());
			viewHolder.tv_softmanager_name.setText(info.getName());
			viewHolder.tv_softmanager_version.setText(info.getVersionName());
			if (info.isSD()){
				viewHolder.tv_softmanager_issd.setText("SD卡");
			}else{
				viewHolder.tv_softmanager_issd.setText("手机内存");
			}
			return view;
		}
		
		class ViewHolder{
			ImageView iv_itemsoftmanage_icon;
			TextView tv_softmanager_name;
			TextView tv_softmanager_issd;
			TextView tv_softmanager_version;
		}
	}
}
