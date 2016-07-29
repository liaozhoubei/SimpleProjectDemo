package com.bproject.zhihuibeijing.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bproject.zhihuibeijing.MainActivity;
import com.bproject.zhihuibeijing.R;
import com.bproject.zhihuibeijing.base.BasePager;
import com.bproject.zhihuibeijing.base.imple.GovAffairsPager;
import com.bproject.zhihuibeijing.base.imple.HomePager;
import com.bproject.zhihuibeijing.base.imple.NewsCenterPager;
import com.bproject.zhihuibeijing.base.imple.SettingPager;
import com.bproject.zhihuibeijing.base.imple.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {

	private ViewPager vp_content;
	private ContentAdapter contentAdapter;
	private List<BasePager> mPages;
	private RadioGroup rg_group;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		vp_content = (ViewPager) view.findViewById(R.id.vp_content);
		rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
		contentAdapter = new ContentAdapter();

		return view;
	}

	@Override
	public void initData() {
		mPages = new ArrayList<BasePager>();

		mPages.add(new HomePager(mActivity));
		mPages.add(new NewsCenterPager(mActivity));
		mPages.add(new GovAffairsPager(mActivity));
		mPages.add(new SmartServicePager(mActivity));
		mPages.add(new SettingPager(mActivity));

		vp_content.setAdapter(contentAdapter);

		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					vp_content.setCurrentItem(0);
					break;
				case R.id.rb_news:
					vp_content.setCurrentItem(1);
					break;

				case R.id.rb_smart:
					vp_content.setCurrentItem(2);
					break;

				case R.id.rb_gov:
					vp_content.setCurrentItem(3);
					break;

				case R.id.rb_setting:
					vp_content.setCurrentItem(4);
					break;

				default:
					break;
				}
			}
		});
		
		vp_content.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mPages.get(position).initData();
				
				if (position == 0 || position == mPages.size() - 1) {
					setSlidingMenuEnable(false);
				} else {
					setSlidingMenuEnable(true);
				}
				
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
		// 在初始的时候刷新 主页 的页面数据
		mPages.get(0).initData();
		setSlidingMenuEnable(false);
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

	private class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPages.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager Pager = mPages.get(position);
			// 调用子类方法填充数据
//			Pager.initData();

			View mRootView = Pager.mRootView;

			container.addView(mRootView);

			return mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
