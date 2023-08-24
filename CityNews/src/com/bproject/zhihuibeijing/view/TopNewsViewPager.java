package com.bproject.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager{

	private float downX;
	private float downY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = ev.getRawX();
			downY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getRawX();
			float moveY = ev.getRawY();
			
			int dx = (int) (moveX - downX);
			int dy = (int) (moveY - downY);
			
			if (Math.abs(dx) > Math.abs(dy)) {
				int currentItem = getCurrentItem();
				// 左右滑动
				if (dx > 0){
					// 向右滑动
					if (currentItem == 0) {
						// 不拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 向左滑动
					int count = getAdapter().getCount();
					if (currentItem == count - 1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
					
				}
			}else {
				// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}

}
