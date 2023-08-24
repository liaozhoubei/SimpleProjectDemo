package com.example.myplaystore.ui.view;

import com.example.myplaystore.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {

	private float ratio;

	public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		ratio = typeArray.getFloat(R.styleable.RatioLayout_ratio, -1);
		typeArray.recycle();
		initView();
	}

	public RatioLayout(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio > 0) {
			int imageWidth = widthSize - getPaddingLeft() - getPaddingRight();
			
			int imageHeight = (int) (widthSize / ratio + 05.f);
			heightSize = imageHeight + getPaddingTop() + getPaddingBottom();
			
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}

}
