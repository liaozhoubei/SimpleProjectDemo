package com.example.myplaystore.ui.activity;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.http.protocol.HomeDetailProtocol;
import com.example.myplaystore.ui.holder.DetailAppInfoHolder;
import com.example.myplaystore.ui.holder.DetailDesHolder;
import com.example.myplaystore.ui.holder.DetailDownloadHolder;
import com.example.myplaystore.ui.holder.DetailPicsHolder;
import com.example.myplaystore.ui.holder.DetailSafeHolder;
import com.example.myplaystore.ui.view.LoadingPager;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HomeDetailActivity extends BaseActivity {
	
	private String packageName;
	private AppInfo data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadingPager loadingPager = new LoadingPager(this){

			@Override
			public View onCreateSuccessView() {
				return HomeDetailActivity.this.onCreateSuccessView();
			}

			@Override
			public ResultState onLoad() {
				return HomeDetailActivity.this.onLoad();
			}
			
		};
		setContentView(loadingPager);
		packageName = getIntent().getStringExtra("packageName");
		System.out.println("packageName" + packageName);
		loadingPager.loadData();
		
	}
	
	public View onCreateSuccessView() {
		View view = UIUtils.inflate(R.layout.page_home_detail);
		FrameLayout fl_detail_appinfo = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
		
		DetailAppInfoHolder detailAppInfoHolder = new DetailAppInfoHolder();
		fl_detail_appinfo.addView(detailAppInfoHolder.getRootView());
		detailAppInfoHolder.setData(data);
		
		FrameLayout fl_detail_safe = (FrameLayout) view.findViewById(R.id.fl_detail_safe);
		DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
		fl_detail_safe.addView(detailSafeHolder.getRootView());
		detailSafeHolder.setData(data);
		
		FrameLayout hsv_detail_pics = (FrameLayout) view.findViewById(R.id.hsv_detail_pics);
		DetailPicsHolder detailPicsHolder = new DetailPicsHolder();
		hsv_detail_pics.addView(detailPicsHolder.getRootView());
		detailPicsHolder.setData(data);
		
		FrameLayout fl_detail_des = (FrameLayout) view.findViewById(R.id.fl_detail_des);
		DetailDesHolder desHolder = new DetailDesHolder();
		fl_detail_des.addView(desHolder.getRootView());
		desHolder.setData(data);
		
		FrameLayout fl_detail_download = (FrameLayout) view.findViewById(R.id.fl_detail_download);
		DetailDownloadHolder detailDownloadHolder = new DetailDownloadHolder();
		fl_detail_download.addView(detailDownloadHolder.getRootView());
		detailDownloadHolder.setData(data);
		
		return view;
	}
	
	public ResultState onLoad() {
		HomeDetailProtocol detailProtocol = new HomeDetailProtocol(packageName);
		data = detailProtocol.getData(0);
		
		if (data != null) {
			return ResultState.STATE_SUCCESS;
		} else {
			return ResultState.STATE_ERROR;
		}
	}
}
	

