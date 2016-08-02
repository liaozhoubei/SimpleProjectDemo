package com.bproject.zhihuibeijing.base.imple.menu;

import java.util.ArrayList;

import com.bproject.zhihuibeijing.MainActivity;
import com.bproject.zhihuibeijing.R;
import com.bproject.zhihuibeijing.base.BaseMenuDetailPager;
import com.bproject.zhihuibeijing.domain.NewsMenu.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{
	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager vp_news_menu_detail;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	
	private ArrayList<NewsTabData> mTabData;
	private ArrayList<TabDetailPager> mTabDetailPager;
	

	public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsTabData> tabData) {
		super(mActivity);
		mTabData = tabData;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void initData() {
		mTabDetailPager = new ArrayList<TabDetailPager>();
		// 初始化页签数据
		for(int i = 0; i < mTabData.size(); i ++) {
			TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
			mTabDetailPager.add(pager);
		}
		
		vp_news_menu_detail.setAdapter(new NewsMenuDetailAdapter());
		mIndicator.setViewPager(vp_news_menu_detail);
		
//		vp_news_menu_detail.setOnPageChangeListener(this);
		mIndicator.setOnPageChangeListener(this);
	}
	
	private class NewsMenuDetailAdapter extends PagerAdapter{

		@Override
		public CharSequence getPageTitle(int position) {
			NewsTabData newsTabData = mTabData.get(position);
			return newsTabData.title;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabDetailPager.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager tabDetailPager = mTabDetailPager.get(position);
			View view = tabDetailPager.mRootView;
			container.addView(view);
			tabDetailPager.initData();
			
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0 ){
			setSlidingMenuEnable(true);
		} else {
			setSlidingMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置是否可滑出策划菜单
	 * @param b
	 */
	private void setSlidingMenuEnable(boolean enable) {
		MainActivity mainActivity = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	@OnClick(R.id.btn_next)
	public void nextpager(View v) {
		int currentItem = vp_news_menu_detail.getCurrentItem();
		currentItem ++;
		vp_news_menu_detail.setCurrentItem(currentItem);
	}

}
