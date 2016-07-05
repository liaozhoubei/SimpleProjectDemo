package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * custom View of activity setting
 * @author Bei
 *
 */
public class SettingClickView extends RelativeLayout {
	private TextView tv_setting_title;
	private TextView tv_setting_des;

	public SettingClickView(Context context) {
		super(context);
		init();
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	private void init() {
		View view = View.inflate(getContext(), R.layout.settingclickview, this);
		tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
		tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);
		
	}
	/**
	 * set the view of title text
	 * @param title
	 */
	public void setTitle(String title) {
		tv_setting_title.setText(title);

	}
	/**
	 * set the view of des text
	 * @param des
	 */
	public void setDes(String des) {
		tv_setting_des.setText(des);
	}

}
