package com.example.mobilesafe;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
/**
 * abstractActivity
 * It is parent-Activity of setupActivity
 * @author Bei
 *
 */
public abstract class SetUpBaseActivity extends Activity{
	// 
	public void pre(View v){
		pre_activity();
	}
	
	public void next(View v){
		next_activity();
	}

	public abstract void pre_activity();
	
	public abstract void next_activity();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK){
			pre_activity();
		}
		return super.onKeyDown(keyCode, event);
	}
}
