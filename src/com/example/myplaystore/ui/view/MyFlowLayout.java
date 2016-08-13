package com.example.myplaystore.ui.view;

import java.util.ArrayList;

import com.example.myplaystore.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyFlowLayout extends ViewGroup {

	private int mUserWidth;
	private int mHorizontalSpace = UIUtils.dip2px(6); // 布局中每个子控件之间的
	private int mVerticalSpacing = UIUtils.dip2px(8);//
	private Line mLine;
	private ArrayList<Line> mLineList = new ArrayList<MyFlowLayout.Line>();
	private static final int MAX_LINE = 100;

	public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyFlowLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = l + getPaddingLeft();
		int top = t + getPaddingTop();

		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			line.layout(left, top);

			top += line.mMaxHeight + mVerticalSpacing;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 整体布局的宽度
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();

		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			int childwidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					(widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : widthMode);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					(heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : heightMode);
			childView.measure(childwidthMeasureSpec, childheightMeasureSpec);

			if (mLine == null) {
				mLine = new Line();
			}

			int childWidth = childView.getMeasuredWidth();

			mUserWidth += childWidth;

			if (mUserWidth < width) {

				mLine.addView(childView);

				mUserWidth += mHorizontalSpace;

				if (mUserWidth > width) {
					// 超出水平宽度时就换行
					if (!newLine()) {
						break;
					}

				}
			} else {
				if (mLine.getchildCount() == 0) {
					mLine.addView(childView);
					if (!newLine()) {
						break;
					}
				} else {
					if (!newLine()) {
						break;
					}
					mLine.addView(childView); // 漏掉的
					mUserWidth = childWidth + mHorizontalSpace;
				}
			}

		}
		if (mLine != null && mLine.getchildCount() != 0 && !mLineList.contains(mLine)) {
			mLineList.add(mLine);
		}

		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int totalHeight = 0;
		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			totalHeight += line.mMaxHeight;
		}

		totalHeight += (mLineList.size() - 1) * mVerticalSpacing;
		totalHeight += getPaddingBottom() + getPaddingTop();

		setMeasuredDimension(totalWidth, totalHeight);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private boolean newLine() {
		mLineList.add(mLine);
		if (mLineList.size() < MAX_LINE) {
			mLine = new Line();
			mUserWidth = 0;
			return true;
		}
		return false;
	}

	// 每一行的封装
	class Line {
		private int mTotalWidth; // 所有控件加起来的宽度
		public int mMaxHeight; // 控件中最高的高度为所有控件的高度
		private ArrayList<View> mChildViewList = new ArrayList<View>();

		public void addView(View view) {
			mChildViewList.add(view);
			mTotalWidth += view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			mMaxHeight = mMaxHeight < height ? height : mMaxHeight;

		}

		public int getchildCount() {
			return mChildViewList.size();
		}

		public void layout(int left, int top) {
			int childCount = getchildCount();
			// 总体可用的宽度
			int validWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
			// 剩余可用的宽度
			int surplusWidth = validWidth - mTotalWidth - (childCount - 1) * mHorizontalSpace;

			if (surplusWidth >= 0) {
				int space = (int) ((float) surplusWidth / childCount + 0.5f);

				// 重新测量
				for (int i = 0; i < childCount; i++) {
					View childView = mChildViewList.get(i);

					int measuredWidth = childView.getMeasuredWidth();
					int measuredHeight = childView.getMeasuredHeight();

					measuredWidth += space;

					int MeasureSpecWidth = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
					int MeasureSpecHeight = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);

					childView.measure(MeasureSpecWidth, MeasureSpecHeight);

					int topOffset = (mMaxHeight - measuredHeight) / 2;

					if (topOffset < 0) {
						topOffset = 0;
					}

					childView.layout(left, top + topOffset, left + measuredWidth, top + topOffset + measuredHeight);
					left += measuredWidth + mHorizontalSpace;
				}
			} else {
				View childView = mChildViewList.get(0);
				childView.layout(left, top, left + childView.getMeasuredWidth(), top + childView.getMeasuredHeight());
			}
		}
	}

}

// public class MyFlowLayout extends ViewGroup {
//
// private int mUsedWidth;// 当前行已使用的宽度
// private int mHorizontalSpacing = UIUtils.dip2px(6);// 水平间距
// private int mVerticalSpacing = UIUtils.dip2px(8);// 竖直间距
//
// private Line mLine;// 当前行对象
//
// private ArrayList<Line> mLineList = new ArrayList<MyFlowLayout.Line>();//
// 维护所有行的集合
//
// private static final int MAX_LINE = 100;// 最大行数是100行
//
// public MyFlowLayout(Context context, AttributeSet attrs, int defStyle) {
// super(context, attrs, defStyle);
// }
//
// public MyFlowLayout(Context context, AttributeSet attrs) {
// super(context, attrs);
// }
//
// public MyFlowLayout(Context context) {
// super(context);
// }
//
// @Override
// protected void onLayout(boolean changed, int l, int t, int r, int b) {
// int left = l + getPaddingLeft();
// int top = t + getPaddingTop();
//
// for (int i = 0; i < mLineList.size(); i++) {
// Line line = mLineList.get(i);
// line.layout(left, top);
//
// top += line.mMaxHeight + mVerticalSpacing;// 更新top值
// }
// }
//
// @Override
// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
// int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() -
// getPaddingRight();
// int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() -
// getPaddingBottom();
//
// int widthMode = MeasureSpec.getMode(widthMeasureSpec);
// int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
// int childCount = getChildCount();
// for (int i = 0; i < childCount; i++) {
// View childView = getChildAt(i);
//
// int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
// (widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : widthMode);
// int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
// (heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : heightMode);
//
//
// childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//
// if (mLine == null) {
// mLine = new Line();
// }
//
// int childWidth = childView.getMeasuredWidth();
//
// mUsedWidth += childWidth;
//
// if (mUsedWidth < width) {
// mLine.addView(childView);
//
// mUsedWidth += mHorizontalSpacing;
//
// if (mUsedWidth > width) {
// if (!newLine()) {
// break;
// }
// }
//
// } else {
// if (mLine.getChildCount() == 0) {
// mLine.addView(childView);
//
// if (!newLine()) {
// break;
// }
// } else {
// if (!newLine()) {
// break;
// }
//
// mLine.addView(childView);
// mUsedWidth += childWidth + mHorizontalSpacing;
// }
// }
//
// }
//
// if (mLine != null && mLine.getChildCount() != 0 &&
// !mLineList.contains(mLine)) {
// mLineList.add(mLine);
// }
//
// int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
//
// int totalHeight = 0;
// for (int i = 0; i < mLineList.size(); i++) {
// Line line = mLineList.get(i);
// totalHeight += line.mMaxHeight;
// }
//
// totalHeight += (mLineList.size() - 1) * mVerticalSpacing;
// totalHeight += getPaddingTop() + getPaddingBottom();
//
// setMeasuredDimension(totalWidth, totalHeight);
// // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
// }
//
// private boolean newLine() {
// mLineList.add(mLine);
//
// if (mLineList.size() < MAX_LINE) {
// mLine = new Line();
// mUsedWidth = 0;
//
// return true;
// }
//
// return false;
// }
//
// class Line {
//
// private int mTotalWidth;
// public int mMaxHeight;
//
// private ArrayList<View> mChildViewList = new ArrayList<View>();
//
// public void addView(View view) {
// mChildViewList.add(view);
// mTotalWidth += view.getMeasuredWidth();
//
// int height = view.getMeasuredHeight();
// mMaxHeight = mMaxHeight < height ? height : mMaxHeight;
// }
//
// public int getChildCount() {
// return mChildViewList.size();
// }
//
// public void layout(int left, int top) {
// int childCount = getChildCount();
//
// int validWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//
// 屏幕总有效宽度
// int surplusWidth = validWidth - mTotalWidth - (childCount - 1) *
// mHorizontalSpacing;
//
// if (surplusWidth >= 0) {
// int space = (int) ((float) surplusWidth / childCount + 0.5f);// 平均每个控件分配的大小
//
// for (int i = 0; i < childCount; i++) {
// View childView = mChildViewList.get(i);
//
// int measuredWidth = childView.getMeasuredWidth();
// int measuredHeight = childView.getMeasuredHeight();
//
// measuredWidth += space;// 宽度增加
//
// int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth,
// MeasureSpec.EXACTLY);
// int heightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight,
// MeasureSpec.EXACTLY);
//
// childView.measure(widthMeasureSpec, heightMeasureSpec);
//
// int topOffset = (mMaxHeight - measuredHeight) / 2;
//
// if (topOffset < 0) {
// topOffset = 0;
// }
//
// childView.layout(left, top + topOffset, left + measuredWidth, top + topOffset
// + measuredHeight);
// left += measuredWidth + mHorizontalSpacing;// 更新left值
// }
//
// } else {
// View childView = mChildViewList.get(0);
// childView.layout(left, top, left + childView.getMeasuredWidth(), top +
// childView.getMeasuredHeight());
// }
//
// }
//
// }
//
// }
