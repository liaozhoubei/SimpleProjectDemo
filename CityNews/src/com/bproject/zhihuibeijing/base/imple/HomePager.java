package com.bproject.zhihuibeijing.base.imple;

import com.bproject.zhihuibeijing.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		
		TextView textview = new TextView(mActivity);
		textview.setText("首页");
		textview.setTextSize(18);
		textview.setTextColor(Color.RED);
		textview.setGravity(Gravity.CENTER);
		flContent.addView(textview);
		
		
		tvTitle.setText("智慧北京");
		btnMenu.setVisibility(View.GONE);
	}

}
