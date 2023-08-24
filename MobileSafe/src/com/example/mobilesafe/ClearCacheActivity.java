package com.example.mobilesafe;

import com.example.mobilesafe.fragment.CacheFragment;
import com.example.mobilesafe.fragment.SDFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class ClearCacheActivity extends FragmentActivity{
	
	private CacheFragment cacheFragment;
	private SDFragment sdFragment;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clearcache);
		
		cacheFragment = new CacheFragment();
		sdFragment = new SDFragment();
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.add(R.id.fram_clearcache_fragment, cacheFragment);
		beginTransaction.add(R.id.fram_clearcache_fragment, sdFragment);
		beginTransaction.hide(sdFragment);
		beginTransaction.commit();
	}

	public void cache(View v){
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.hide(sdFragment);
		beginTransaction.show(cacheFragment);
		beginTransaction.commit();
	}

	public void sd(View v){
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.hide(cacheFragment);
		beginTransaction.show(sdFragment);
		beginTransaction.commit();
	}
}
