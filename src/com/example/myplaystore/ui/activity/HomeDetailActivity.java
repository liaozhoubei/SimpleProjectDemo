package com.example.myplaystore.ui.activity;

import com.example.myplaystore.ui.view.LoadingPager;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.utils.UIUtils;

import android.os.Bundle;
import android.view.View;

public class HomeDetailActivity extends BaseActivity {
	
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
		loadingPager.onLoad();
	}
	
	public View onCreateSuccessView() {
		return null;
	}
	
	public ResultState onLoad() {
		return null;
	}
}
