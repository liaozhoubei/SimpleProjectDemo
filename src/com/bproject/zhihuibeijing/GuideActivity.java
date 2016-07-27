package com.bproject.zhihuibeijing;

import java.util.ArrayList;
import java.util.List;

import com.bproject.zhihuibeijing.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {

	private Button btn_start;
	private LinearLayout ll_container;
	private ViewPager vp_guide;
	private List<ImageView> mImageViewList;
	private GuideAdapter guideAdapter;
	private ImageView iv_red_point;
	private int mPointDis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		mImageViewList = new ArrayList<ImageView>();
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		btn_start = (Button) findViewById(R.id.btn_start);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
		
		guideAdapter = new GuideAdapter();
		initData();
		initAdapter();
		initListener();
		
		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PrefUtils.setBoolean(getApplicationContext(), "is_First_enter", false);
				Intent intent = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}



	private void initData() {
		int [] mImageIds = new int[] {R.drawable.guide_1, R.drawable.guide_2,R.drawable.guide_3};
		ImageView imageView;
		ImageView points;
		for (int i = 0; i < mImageIds.length; i ++){
			imageView = new ImageView(GuideActivity.this);
			imageView.setBackgroundResource(mImageIds[i]);
			mImageViewList.add(imageView);
			
			points = new ImageView(GuideActivity.this);
			points.setImageResource(R.drawable.shape_point_gray);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				params.leftMargin = 10;
			}
			points.setLayoutParams(params);
			ll_container.addView(points);
			
		}
		
	}
	
	private void initListener() {
		vp_guide.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (position == mImageViewList.size() - 1) {
					btn_start.setVisibility(View.VISIBLE);
				} else {
					btn_start.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				int leftMargin = (int) (mPointDis * positionOffset + 
						mPointDis * position);
				System.out.println("leftMargin: " + "positionOffset: " + positionOffset + leftMargin + "    mPointDis : " + mPointDis + "    position: " + position);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
				params.leftMargin = leftMargin;
				//	改变红色小圆点的举例
				iv_red_point.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mPointDis = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
			}
		});
	}

	private void initAdapter() {
		vp_guide.setAdapter(guideAdapter);
	}
	
	private class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = mImageViewList.get(position);
			container.addView(imageView);
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}


}
