package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.utils.UIUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailDesHolder extends BaseHolder<AppInfo> {
	private TextView tvDes;
	private TextView tvAuthor;
	private ImageView ivArrow;
	private RelativeLayout rlToggle;
	private boolean isOpen = false;
	private LinearLayout.LayoutParams layoutParams;
	
	
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_desinfo);

		tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
		tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		rlToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
		
		rlToggle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
		
		return view;
	}
	
	@Override
	public void refreshView(AppInfo data) {
		tvDes.setText(data.des);
		tvAuthor.setText(data.author);
		
		tvDes.post(new Runnable() {
			
			@Override
			public void run() {
				int shortHeight = getShortHeight();
				layoutParams = (LayoutParams) tvDes.getLayoutParams();
				layoutParams.height = shortHeight;
				tvDes.setLayoutParams(layoutParams);
				
				
			}
		});
		
	}


	protected void toggle() {
		int shortHeight = getShortHeight();
		int longHeight = getLongHeight();
		ValueAnimator animator = null;
		
		if (isOpen) {
			isOpen = false;
			if (longHeight > shortHeight) {
				animator = ValueAnimator.ofInt(longHeight, shortHeight);
				
			}
		} else {
			isOpen = true;
			if (longHeight > shortHeight) {
				animator = ValueAnimator.ofInt(shortHeight, longHeight);
			}
		}
		
		if (animator != null) {
			animator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int height = (Integer) animation.getAnimatedValue();
					layoutParams.height = height;
					
					tvDes.setLayoutParams(layoutParams);
					
				}
			});
			
			
			animator.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					final ScrollView scrollView = getScrollView();
					scrollView.post(new Runnable() {
						
						@Override
						public void run() {
							scrollView.fullScroll(ScrollView.FOCUS_DOWN);
							
						}
					});
					
					if (isOpen) {
						ivArrow.setImageResource(R.drawable.arrow_up);
					} else {
						ivArrow.setImageResource(R.drawable.arrow_down);
					}
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
			});
			
			animator.setDuration(200);
			animator.start();
		}
		
	}


	
	public int getShortHeight(){
		int measuredWidth = tvDes.getMeasuredWidth();
		TextView textView = new TextView(UIUtils.getContext());
		textView.setText(getData().des);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		textView.setMaxLines(7);
		
		int measureSpecWidth = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
		int measureSpecHeight = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
		
		textView.measure(measureSpecWidth, measureSpecHeight);
		
		return textView.getMeasuredHeight();
		
	}
	
	public int getLongHeight(){
		int measuredWidth = tvDes.getMeasuredWidth();
		
		TextView textView = new TextView(UIUtils.getContext());
		textView.setText(getData().des);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		int makeMeasureSpecWidth = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
		int makeMeasureSpecHeight = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
		textView.measure(makeMeasureSpecWidth, makeMeasureSpecHeight);
		
		return textView.getMeasuredHeight();
	}
	
	private ScrollView getScrollView(){
		ViewParent viewParent = tvDes.getParent();
		while (!(viewParent instanceof ScrollView)) {
			viewParent = viewParent.getParent();
		}
		return (ScrollView) viewParent;
	}

}
