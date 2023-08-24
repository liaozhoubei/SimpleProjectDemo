package com.bproject.zhihuibeijing.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bproject.zhihuibeijing.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener{
	
	private static final int STATE_PULL_TO_REFRESH = 1; // 下拉刷新状态
	private static final int STATE_RELEASE_TO_REFRESH = 2; // 释放刷新状态
	private static final int STATE_REFRESHING = 3;	// 正在刷新状态
	private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前刷新状态

	private View mHeaderView;
	private int mHeaderViewHeight;
	private RotateAnimation rotateUpAnimation;
	private RotateAnimation rotateDownAnimation;
	private float downY;
	private ImageView iv_arrow;
	private ProgressBar pb_loading;
	private TextView tv_title;
	private TextView tv_time;
	private OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();

	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public PullToRefreshListView(Context context) {
		super(context);
		init();
	}

	private void init() {
		initHeaderView();
		initFooterView();
		initAnimation();
	}



	private void initAnimation() {
		rotateUpAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		rotateUpAnimation.setDuration(300);
		rotateUpAnimation.setFillAfter(true);
		
		rotateDownAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		rotateDownAnimation.setDuration(300);
		rotateDownAnimation.setFillAfter(true);
	}

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pb_loading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, - mHeaderViewHeight, 0, 0);
		addHeaderView(mHeaderView);
		
		setCurrentTime();
	}
	
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, - mFooterViewHeight, 0, 0);
		addFooterView(mFooterView);
		// 设置滑动时监听
		this.setOnScrollListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			if (downY == -1) {// 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,
				// 导致startY没有赋值,此处需要重新获取一下
				downY = (int) ev.getY();
			}	
			
			if (mCurrentState == STATE_REFRESHING) {
				// 如果是正在刷新, 跳出循环
				break;
			}
			
			float moveY = ev.getY();
			
			int offset = (int) (moveY - downY);
			if (offset > 0 && getFirstVisiblePosition() == 0){
				int paddingTop = - mHeaderViewHeight + offset;
				mHeaderView.setPadding(0, paddingTop, 0, 0);
				
				if (paddingTop > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH){
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					updataView();
				} else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					mCurrentState = STATE_PULL_TO_REFRESH;
					updataView();
				}
				return true;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			downY = -1;

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				updataView();

				// 完整展示头布局
				mHeaderView.setPadding(0, 0, 0, 0);

				// 4. 进行回调
				if (mListener != null) {
					mListener.onRefresh();
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			
			
			break;

		default:
			break;
		}
		
		return super.onTouchEvent(ev);
	}
	
	private void updataView(){
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			iv_arrow.startAnimation(rotateDownAnimation);
			tv_title.setText("下拉刷新");
			break;
		case STATE_RELEASE_TO_REFRESH:
			iv_arrow.startAnimation(rotateUpAnimation);
			tv_title.setText("释放刷新");
			break;
		case STATE_REFRESHING:
			iv_arrow.clearAnimation(); // 清除动画，否则无法隐藏
			iv_arrow.setVisibility(View.INVISIBLE);
			pb_loading.setVisibility(View.VISIBLE);
			tv_title.setText("正在刷新");
			
			break;

		default:
			break;
		}
	}
	// 设置时间
	private void setCurrentTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(new Date());
		tv_time.setText(time);
	}
	
	
	public void onRefreshComplete(boolean success){
		if (!isLoadMore) {
			
			mHeaderView.setPadding(0, - mHeaderViewHeight, 0, 0);
			mCurrentState = STATE_PULL_TO_REFRESH;
			iv_arrow.setVisibility(View.VISIBLE);
			pb_loading.setVisibility(View.INVISIBLE);
			tv_title.setText("下拉刷新");
			if (success) {
				setCurrentTime();
			}
		} else {
			mFooterView.setPadding(0, - mFooterViewHeight, 0, 0);
			isLoadMore = false;
		}
	}
	
	public void setOnRefreshListener(OnRefreshListener listener){
		mListener = listener;
	}
	
	public interface OnRefreshListener{
		public void onRefresh();
		public void onLoadMore();
	}
	
	private boolean isLoadMore;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_IDLE:
			// listView空闲时
			if (getCount() - 1 == getLastVisiblePosition() && !isLoadMore) {
				isLoadMore = true;
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			// 手指滑动时
			break;
		case SCROLL_STATE_FLING:
			// 快速滑翔时
			break;

		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}	
	

}
