package com.example.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 设置移动号码归属地视图
 * @author Bei
 *
 */
public class DragViewActivity extends Activity {
	private LinearLayout mLl_dragview_toast;
	private SharedPreferences sp;
	private WindowManager windowManager;
	private int widthPixels;
	private int heightPixels;
	long[] mHits = new long[2];
	private TextView tv_dragview_top;
	private TextView tv_dragview_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragview);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		mLl_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
		tv_dragview_top = (TextView)findViewById(R.id.tv_dragview_top);
		tv_dragview_bottom = (TextView)findViewById(R.id.tv_dragview_bottom);
		
		
		int x = sp.getInt("X", 0);
		int y = sp.getInt("Y", 0);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mLl_dragview_toast.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		mLl_dragview_toast.setLayoutParams(params);
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		widthPixels = outMetrics.widthPixels;
		heightPixels = outMetrics.heightPixels;
		
		
		setTouch();
		setDoubleClick();
	}
	/**
	 * 设置双击视图居中
	 */
	private void setDoubleClick() {
		mLl_dragview_toast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
		            mHits[mHits.length-1] = SystemClock.uptimeMillis();
		            if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {
		            	
		            	int l = (widthPixels - mLl_dragview_toast.getWidth()) / 2; 
		            	int t = (heightPixels - mLl_dragview_toast.getHeight()) / 2;
		            	mLl_dragview_toast.layout(l, t, l + mLl_dragview_toast.getWidth(), t + mLl_dragview_toast.getHeight());
		            	
		            }
		            
				
			}
		});
	}
	/**
	 * 设置触摸移动
	 */
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
					
					// 限定号码归属地视图不能移出屏幕区域
					if (l < 0 || t < 0 || r > widthPixels || b > heightPixels) {
						break;
					}
					
			
					
					mLl_dragview_toast.layout(l, t, r, b);
					
					// 当号码归属地移动时，上下文字会相应的改变
					int top = mLl_dragview_toast.getTop();
					if (top > heightPixels /2 ){
						tv_dragview_top.setVisibility(View.INVISIBLE);
						tv_dragview_bottom.setVisibility(View.VISIBLE);
					} else {
						tv_dragview_top.setVisibility(View.VISIBLE);
						tv_dragview_bottom.setVisibility(View.INVISIBLE);
					}
					
					mStartX = newX;
					mStartY = newY;
					
		
					break;
				case MotionEvent.ACTION_UP:
					Editor edit = sp.edit();
					// 获取号码归属地视图的坐标
					int x = mLl_dragview_toast.getLeft();
					int y = mLl_dragview_toast.getTop();
					
					edit.putInt("X", x);
					edit.putInt("Y", y);
					edit.commit();
					
					break;

				default:
					break;
				}
				// 在只有触摸事件的时候用true，否则不生效。在触摸和点击事件并存是用false，否则会起冲突，造成点击失效
				return false;
			}
		});
	}
}
