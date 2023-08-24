package com.example.myplaystore.ui.activity;

import com.example.myplaystore.R;
import com.example.myplaystore.ui.fragment.BaseFragment;
import com.example.myplaystore.ui.fragment.FragmentFactory;
import com.example.myplaystore.ui.view.PagerTab;
import com.example.myplaystore.utils.UIUtils;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends BaseActivity {

	private PagerTab pager_tab;
	private ViewPager viewpager;
	private MyFragmentAdapter adapter;
	private ActionBarDrawerToggle drawerToggle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 显示ActionBar的图标
//		ActionBar actionBar = getSupportActionBar();  
//		actionBar.setDisplayShowHomeEnabled(true);  
//		actionBar.setLogo(R.drawable.ic_launcher);  // 展示logo
//		actionBar.setDisplayUseLogoEnabled(true);  
//		actionBar.setHomeButtonEnabled(true);  //图标可点击
//		actionBar.setDefaultDisplayHomeAsUpEnabled(true); // 显示返回键
		
		
		initActionBar();
		
		pager_tab = (PagerTab) findViewById(R.id.pager_tab);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		
		adapter = new MyFragmentAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		pager_tab.setViewPager(viewpager);
		pager_tab.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BaseFragment baseFragment = FragmentFactory.createFragment(position);
				baseFragment.loadData();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	private class MyFragmentAdapter extends FragmentPagerAdapter{
		

		private String[] tabNameArrays;

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			tabNameArrays = UIUtils.getStringArray(R.array.tab_names);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return tabNameArrays[position];
		}

		@Override
		public Fragment getItem(int position) {
			BaseFragment createFragment = FragmentFactory.createFragment(position);
			return createFragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabNameArrays.length;
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void initActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ic_launcher);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
		drawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer_am, R.string.drawer_open,
				R.string.drawer_close);
		drawerToggle.syncState();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			drawerToggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
}
