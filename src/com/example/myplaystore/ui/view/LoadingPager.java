package com.example.myplaystore.ui.view;

import com.example.myplaystore.R;
import com.example.myplaystore.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public abstract class LoadingPager extends FrameLayout {
	
	private static final int STATE_LOAD_UNDO = 1;// 未加载
	private static final int STATE_LOAD_LOADING = 2;// 正在加载
	private static final int STATE_LOAD_ERROR = 3;// 加载失败
	private static final int STATE_LOAD_EMPTY = 4;// 数据为空
	private static final int STATE_LOAD_SUCCESS = 5;// 加载成功

	private int mCurrentState = STATE_LOAD_UNDO;// 当前状态
	private View mLoadingPager;
	private View mErrorPage;
	private View mEmptyPager;
	private View mSuccessPager;

	public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public LoadingPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LoadingPager(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		if (mLoadingPager == null) {
			mLoadingPager = UIUtils.getView(R.layout.page_loading);
			addView(mLoadingPager);
		}
		
		if (mErrorPage == null) {
			mErrorPage = UIUtils.getView(R.layout.page_error);
			Button btn_retry = (Button) mErrorPage.findViewById(R.id.btn_retry);
			btn_retry.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadData();
				}
			});
			addView(mErrorPage);
		}
		
		
		if (mEmptyPager == null) {
			mEmptyPager = UIUtils.getView(R.layout.page_empty);
			addView(mEmptyPager);
		}
		
		showRightPager();
		
	}

	private void showRightPager() {
		if (mCurrentState == STATE_LOAD_LOADING || mCurrentState == STATE_LOAD_UNDO) {
			mLoadingPager.setVisibility(View.VISIBLE);
		} else {
			mLoadingPager.setVisibility(View.GONE);
		}
		
		mErrorPage.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);
		
		mEmptyPager.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);
		
		if (mSuccessPager == null && mCurrentState == STATE_LOAD_SUCCESS) {
			mSuccessPager = onCreateSuccessView();
			if (mSuccessPager != null) {
				addView(mSuccessPager);
			}
		}
		
		if (mSuccessPager != null) {
			mSuccessPager.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
		}
		
	}
	
	public void loadData(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				final ResultState resultState = onLoad();
				UIUtils.runOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						if (resultState != null) {
							mCurrentState = resultState.getState();
							showRightPager();
						}
					}
				});
			}
		}.start();
	}

	
	public enum ResultState{
		STATE_SUCCESS(STATE_LOAD_SUCCESS), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_ERROR(STATE_LOAD_ERROR);
		private int state;
		private ResultState(int state) {
			this.state = state;
		}
		
		public int getState(){
			return state;
		}
	}
	

	
	
	public abstract View onCreateSuccessView();

	public abstract ResultState onLoad();
}
