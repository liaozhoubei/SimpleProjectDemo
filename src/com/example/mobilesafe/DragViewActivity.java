package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
/**
 * 设置移动号码归属地视图
 * @author Bei
 *
 */
public class DragViewActivity extends Activity {
	private LinearLayout mLl_dragview_toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragview);
		mLl_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
		setTouch();
		
	}

	private void setTouch() {
		mLl_dragview_toast.setOnTouchListener(new OnTouchListener() {
			
			private int mStartX;
			private int mStartY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mStartX = (int) event.getRawX();
					mStartY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX  = (int) event.getRawX();
					int newY = (int) event.getRawY();
					
					int dx = newX - mStartX;
					int dy = newY - mStartY;
					int l = mLl_dragview_toast.getLeft();
					int t = mLl_dragview_toast.getTop();
					l += dx;
					t += dy;
					int r = l + mLl_dragview_toast.getWidth();
					int b = t + mLl_dragview_toast.getHeight();
					
					mLl_dragview_toast.layout(l, t, r, b);
					
					mStartX = newX;
					mStartY = newY;
					
		
					break;
				case MotionEvent.ACTION_UP:
					
					break;

				default:
					break;
				}
				return true;
			}
		});
	}
}
