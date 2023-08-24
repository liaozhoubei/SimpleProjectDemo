package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * custom View of activity setting
 * @author Bei
 *
 */
public class SettingView extends RelativeLayout {
	private TextView tv_setting_title;
	private TextView tv_setting_des;
	private CheckBox cb_setting_update;
	private String des_on;
	private String des_off;

	public SettingView(Context context) {
		super(context);
		init();
	}
	
	

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// get the value in layout
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.settingview, 0, 0);
		String title = typedArray.getString(R.styleable.settingview_title);
		des_on = typedArray.getString(R.styleable.settingview_des_on);
		des_off = typedArray.getString(R.styleable.settingview_des_off);
		tv_setting_title.setText(title);
		
		if(isChecked()){
			tv_setting_des.setText(des_on);
		} else{
			tv_setting_des.setText(des_off);
		}
	}



	public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}



	private void init() {
		View view = View.inflate(getContext(), R.layout.setting_view, this);
		tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
		tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);
		cb_setting_update = (CheckBox) view.findViewById(R.id.cb_setting_update);
		
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
	/**
	 * set the boolean of checkBox
	 * @param isChecked
	 */
	public void setChecked(boolean isChecked){
		cb_setting_update.setChecked(isChecked);
		if(isChecked()){
			tv_setting_des.setText(des_on);
		} else{
			tv_setting_des.setText(des_off);
		}
	}
	/**
	 * get the state of checkBox
	 * @return
	 */
	public boolean isChecked(){
		return cb_setting_update.isChecked();
	}

}
