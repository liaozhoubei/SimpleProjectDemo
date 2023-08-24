package com.example.myplaystore.ui.holder;

import java.util.ArrayList;

import com.example.myplaystore.R;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {
	private ArrayList<String> data;
	private ViewPager viewPager;
	private ImageView points;
	private LinearLayout linearLayout;

	@Override
	public View initView() {
		RelativeLayout rlLayout = new RelativeLayout(UIUtils.getContext());
		AbsListView.LayoutParams absListViewParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				UIUtils.dip2px(150));
		rlLayout.setLayoutParams(absListViewParams);
		
		
		viewPager = new ViewPager(UIUtils.getContext());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		rlLayout.addView(viewPager, layoutParams);
		
		linearLayout = new LinearLayout(UIUtils.getContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		int padding = UIUtils.dip2px(10);
		linearLayout.setPadding(padding, padding, padding, padding);
		// 设置指示器参数
		RelativeLayout.LayoutParams llparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		llparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		llparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		rlLayout.addView(linearLayout, llparams);
		return rlLayout;
	}

	@Override
	public void refreshView( ArrayList<String> data) {
		this.data = data;
		viewPager.setAdapter(new HeaderHolderAdapter());
		for (int i = 0 ; i < data.size(); i ++) {
			ImageView point = new ImageView(UIUtils.getContext());
			// 设置point的宽高值为包裹内容
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i == 0){
				point.setImageResource(R.drawable.indicator_selected);
			} else {
				params.leftMargin= UIUtils.dip2px(4);
				point.setImageResource(R.drawable.indicator_normal);
			}
			linearLayout.addView(point, params);
			
		}
		
		// 设置当前轮播图位置，因为轮播图如果在第一张图时无法滑动到上一张
		viewPager.setCurrentItem(data.size() * 10000);
		HomeHeaderTast headerTast = new HomeHeaderTast();
		headerTast.start();
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			private int mPreviousPos;

			@Override
			public void onPageSelected(int position) {
				position = position % HomeHeaderHolder.this.data.size();
				ImageView childAt = (ImageView) linearLayout.getChildAt(position);
				childAt.setImageResource(R.drawable.indicator_selected);
				
				// 设置上次的点为不被选择
				ImageView childAt2 = (ImageView) linearLayout.getChildAt(mPreviousPos);
				childAt2.setImageResource(R.drawable.indicator_normal);
				mPreviousPos = position;
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
