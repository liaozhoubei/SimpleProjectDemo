package com.bproject.zhihuibeijing.base.imple;

import com.bproject.zhihuibeijing.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		
		TextView textview = new TextView(mActivity);
		textview.setText("智慧服务");
		textview.setTextSize(18);
		textview.setTextColor(Color.RED);
		textview.setGravity(Gravity.CENTER);
		flContent.addView(textview);
		
		
		tvTitle.setText("智慧服务");
		btnMenu.setVisibility(View.VISIBLE);
	}

}
