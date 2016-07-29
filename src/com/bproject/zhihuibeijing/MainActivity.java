package com.bproject.zhihuibeijing;

import com.bproject.zhihuibeijing.fragment.ContentFragment;
import com.bproject.zhihuibeijing.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends SlidingFragmentActivity {
	
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(200);
		initFragment();
	}

	private void initFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
		transaction.add(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		transaction.commit();
	}
	
	public LeftMenuFragment getLeftFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		LeftMenuFragment leftFragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(TAG_LEFT_MENU);
		
		return leftFragment;
	}
}
