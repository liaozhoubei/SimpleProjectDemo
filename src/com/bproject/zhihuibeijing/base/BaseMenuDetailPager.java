package com.bproject.zhihuibeijing.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
	public Activity mActivity;
	public View mRootView;// 菜单详情页根布局

	public BaseMenuDetailPager(Activity mActivity) {
		super();
		this.mActivity = mActivity;
		mRootView = initView();
	}
	
	public abstract View initView();
	
	public void initData(){
		
	}
	
}
