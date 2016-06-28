package com.example.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
/**
 * abstractActivity
 * It is parent-Activity of setupActivity
 * @author Bei
 *
 */
public abstract class SetUpBaseActivity extends Activity{
	private GestureDetector mGestureDetector;
	protected SharedPreferences sp;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);				
		mGestureDetector = new GestureDetector(getApplicationContext(), new GestureListener());
	}
	
	// register the TouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	// ScreenListener
	public class GestureListener extends SimpleOnGestureListener{

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			float startpositon = e1.getRawX();
			float endposition = e2.getRawX();
			if ((endposition - startpositon) > 100) {
				pre_activity();
			}
			if ((startpositon - endposition) > 100) {
				next_activity();
			}
			return true;
		}
		
	}
	
	// button of previous
	public void pre(View v){
		pre_activity();
	}
	
	// button of next
	public void next(View v){
		next_activity();
	}

	// Subclass to achieve the specific behavior of the parent class
	public abstract void pre_activity();
	
	public abstract void next_activity();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			pre_activity();
		}
		return super.onKeyDown(keyCode, event);
	}
}
