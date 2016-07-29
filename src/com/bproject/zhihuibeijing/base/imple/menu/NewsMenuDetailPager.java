package com.bproject.zhihuibeijing.base.imple.menu;

import com.bproject.zhihuibeijing.base.BaseMenuDetailPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class NewsMenuDetailPager extends BaseMenuDetailPager {

	public NewsMenuDetailPager(Activity mActivity) {
		super(mActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView view = new TextView(mActivity);
		view.setText("菜单详情页-新闻");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		return view;
	}

}
