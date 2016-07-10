package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.engine.AppEngine;
import com.example.mobilesafe.utils.AppUtil;
import com.example.mobilesafe.utils.MyAsycnTaks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SoftMangaerActivity extends Activity implements OnClickListener{
	private ListView mLv_softmanager_application;
	private ProgressBar mLoading;
	private List<AppInfo> appInfos;
	private List<AppInfo> userInofs;
	private List<AppInfo> systemInfos;
	private TextView tv_softmanager_userorsystem;
	private AppInfo info;
	private PopupWindow mPopupWindow;
	private AppInfoAdapter appInfoAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_softmanager);
		mLoading = (ProgressBar) findViewById(R.id.loading);
		mLv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
		tv_softmanager_userorsystem = (TextView)findViewById(R.id.tv_softmanager_userorsystem);
		TextView tv_softmanager_rom = (TextView) findViewById(R.id.tv_softmanager_rom);
		TextView tv_softmanager_sd= (TextView) findViewById(R.id.tv_softmanager_sd);
		long availableSD = AppUtil.getAvailableSD();
		long availableROM = AppUtil.getAvailableROM();
		String formatFileSizeSD = Formatter.formatFileSize(getApplicationContext(), availableSD);
		String formatFileSizeRom = Formatter.formatFileSize(getApplicationContext(), availableROM);
		tv_softmanager_rom.setText("Rom可用：" + formatFileSizeRom);
		tv_softmanager_sd.setText("SD卡可用空间：" + formatFileSizeSD);
		
		
		
		fillData();
		listViewOnScroll();
		listViewItemClick();
	}
	private void listViewItemClick() {
		mLv_softmanager_application.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0 || position == userInofs.size() + 1) {
					return;
				} 
				if (position <= userInofs.size() - 1) {
					info = userInofs.get(position);
				} else  {
					info = systemInfos.get(position - userInofs.size());
				}
				hidePopupWindow();
				View contentView = View.inflate(getApplicationContext(), R.layout.popu_window, null);
				//初始化控件
				LinearLayout ll_popuwindow_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_uninstall);
				LinearLayout ll_popuwindow_start = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_start);
				LinearLayout ll_popuwindow_share = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_share);
				LinearLayout ll_popuwindow_detail = (LinearLayout) contentView.findViewById(R.id.ll_popuwindow_detail);
				//给控件设置点击事件
				ll_popuwindow_uninstall.setOnClickListener(SoftMangaerActivity.this);
				ll_popuwindow_start.setOnClickListener(SoftMangaerActivity.this);
				ll_popuwindow_share.setOnClickListener(SoftMangaerActivity.this);
				ll_popuwindow_detail.setOnClickListener(SoftMangaerActivity.this);
				
				// 获得Item的坐标位置，x为横坐标，y为纵坐标
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int x = location[0];
				System.out.println(x+ "横坐标");
				
				int y = location[1];
				System.out.println(y + "纵坐标");
				mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				mPopupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,  x + 100, y);
				
				// 设置动画效果
				//前四个参数 :　控制控件由没有变到有   动画 0:没有    1:整个控件
				//后四个参数:控制控件是按照自身还是父控件进行变化
				ScaleAnimation scale = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(500);
				
				AlphaAnimation alpha = new AlphaAnimation(0.4f, 1.0f);
				
				AnimationSet set = new AnimationSet(true);
				set.addAnimation(scale);
				set.addAnimation(alpha);
				contentView.startAnimation(set);
			}
		});
	}
	// 浮动显示用户程序或系统程序的个数
	private void listViewOnScroll() {
		mLv_softmanager_application.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (userInofs != null && systemInfos != null) {
					if (firstVisibleItem <= userInofs.size() -1) {
						tv_softmanager_userorsystem.setText("用户程序(" + userInofs.size() + ")");
					} else {
						tv_softmanager_userorsystem.setText("系统程序(" + systemInfos.size() + ")");
					}
				}
			}
		});
	}
	// 使用异步加载的方法加载程序数据
	private void fillData() {
		new MyAsycnTaks() {
			
			@Override
			public void preTask() {
				mLoading.setVisibility(View.VISIBLE);				
			}
			
			@Override
			public void postTast() {
				if (appInfoAdapter != null){
					appInfoAdapter.notifyDataSetChanged();
				} else {
					appInfoAdapter = new AppInfoAdapter();
					mLv_softmanager_application.setAdapter(appInfoAdapter);
					
				}
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
	// 隐藏气泡的方法
	private void hidePopupWindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
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
			
			if (position == 0 ) {
				TextView text = new TextView(getApplicationContext());
				text.setText("用户程序(" + userInofs.size() + ")");
				text.setBackgroundColor(Color.GRAY);
				text.setTextColor(Color.WHITE);
				return text;
			} else if ( position == (userInofs.size() + 1)) {
				TextView text = new TextView(getApplicationContext());
				text.setText("系统程序(" + systemInfos.size() + ")");
				text.setBackgroundColor(Color.GRAY);
				text.setTextColor(Color.WHITE);
				return text;
			}
			
			ViewHolder viewHolder;
			View view;
			// convertView instanceof RelativeLayout表示convertView是RelativeLayout的实例，这可以防止上面的TextView text不被复用
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			} else {
				
				view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_itemsoftmanage_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanage_icon);
				viewHolder.tv_softmanager_name = (TextView) view.findViewById(R.id.tv_softmanager_name);
				viewHolder.tv_softmanager_issd = (TextView) view.findViewById(R.id.tv_softmanager_issd);
				viewHolder.tv_softmanager_version = (TextView) view.findViewById(R.id.tv_softmanager_version);
				view.setTag(viewHolder);
			}
			AppInfo info;
			if (position <= userInofs.size() - 1) {
				info = userInofs.get(position);
			} else  {
				info = systemInfos.get(position - userInofs.size());
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		hidePopupWindow();
	}

	// 气泡通知栏点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_popuwindow_uninstall:
			uninstall();
			break;
			
		case R.id.ll_popuwindow_start:
			startApp();
			break;
		case R.id.ll_popuwindow_share:
			share();
			break;
		case R.id.ll_popuwindow_detail:
			showDetail();
			break;

		default:
			break;
		}
		hidePopupWindow();
	}
	// 将app信息分享给其他app
	private void share() {
		/*
		 * <action android:name="android.intent.action.SEND" />
               <category android:name="android.intent.category.DEFAULT" />
               <data android:mimeType="text/plain" />
		 */
		
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "这是一个很牛逼的软件，你信不信");
		startActivity(intent);
	}
	// 显示app详情
	private void showDetail() {
		
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + info.getPackagName()));
		startActivity(intent);
	}
	// 从软件管理中启动其他App
	private void startApp() {
		PackageManager manager = getPackageManager();
		Intent launchIntentForPackage = manager.getLaunchIntentForPackage(info.getPackagName());
		if (launchIntentForPackage != null) {
			startActivity(launchIntentForPackage);
		}
	}
	// 卸载应用
	private void uninstall() {
		/* <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <action android:name="android.intent.action.DELETE" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="package" />
     </intent-filter>*/
		if (info.getPackagName().equals(getPackageName())){
			Toast.makeText(getApplicationContext(), "无法删除自己", Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + info.getPackagName()));
			startActivityForResult(intent, 0);
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}
}
