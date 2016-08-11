package com.example.myplaystore.ui.holder;

import java.util.ArrayList;

import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {
	private ArrayList<String> data;
	private ViewPager viewPager;

	@Override
	public View initView() {
		RelativeLayout rlLayout = new RelativeLayout(UIUtils.getContext());
		AbsListView.LayoutParams absListViewParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				UIUtils.dip2px(150));
		rlLayout.setLayoutParams(absListViewParams);
		
		
		viewPager = new ViewPager(UIUtils.getContext());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		rlLayout.addView(viewPager, layoutParams);
		return rlLayout;
	}

	@Override
	public void refreshView(ArrayList<String> data) {
		this.data = data;
		viewPager.setAdapter(new HeaderHolderAdapter());
		// 设置当前轮播图位置，因为轮播图如果在第一张图时无法滑动到上一张
		viewPager.setCurrentItem(data.size() * 10000);
		HomeHeaderTast headerTast = new HomeHeaderTast();
		headerTast.start();
	}
	
	private class HeaderHolderAdapter extends PagerAdapter{
		
		private BitmapUtils bitmapUtils;
		
		public HeaderHolderAdapter() {
			bitmapUtils = BitmapHelper.getBitmapUtils();
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % data.size();
			String url = data.get(position);
			ImageView imageView = new ImageView(UIUtils.getContext());
			imageView.setScaleType(ScaleType.CENTER_CROP);
			bitmapUtils.display(imageView, HttpHelper.URL + "image?name=" + url);
			container.addView(imageView);
			
			return imageView;
		}
		
		
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}
	// 执行viewpager自动轮播
	private class HomeHeaderTast implements Runnable{

		public void start(){
			UIUtils.getHandler().removeCallbacksAndMessages(null);
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		
		@Override
		public void run() {
			int currentPosition = viewPager.getCurrentItem();
			currentPosition ++;
			viewPager.setCurrentItem(currentPosition);
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		
	}

}
