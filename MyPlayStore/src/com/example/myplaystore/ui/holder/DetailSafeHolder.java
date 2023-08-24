package com.example.myplaystore.ui.holder;

import java.util.ArrayList;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.AppInfo.SafeInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailSafeHolder extends BaseHolder<AppInfo>{
	
	private ImageView[] mSafeIcons;
	private ImageView[] mDesIcons;
	private TextView[] mSafeDes;
	
	private LinearLayout[] mSafeDesBar;// 安全描述条目(图片+文字)
	private BitmapUtils mBitmapUtils;

	private RelativeLayout rlDesRoot;
	private LinearLayout llDesRoot;
	private ImageView ivArrow;

	private LinearLayout.LayoutParams layoutParams;
	private int measuredHeight;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_safeinfo);
		mSafeIcons = new ImageView[4];
		mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
		mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
		mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
		mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);
		
		mDesIcons = new ImageView[4];
		mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
		mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
		mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
		mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);
		
		mSafeDes = new TextView[4];
		mSafeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
		mSafeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
		mSafeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
		mSafeDes[3] = (TextView) view.findViewById(R.id.tv_des4);
		
		mSafeDesBar = new LinearLayout[4];
		mSafeDesBar[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
		mSafeDesBar[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
		mSafeDesBar[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
		mSafeDesBar[3] = (LinearLayout) view.findViewById(R.id.ll_des4);
		
		rlDesRoot = (RelativeLayout) view.findViewById(R.id.rl_des_root);
		rlDesRoot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
		llDesRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);
		ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		
		return view;
	}

	private boolean isOpen = false;
	
	protected void toggle() {
		ValueAnimator valueAnimator = null;
		if (isOpen) {
			isOpen = false;
			valueAnimator = ValueAnimator.ofInt(measuredHeight, 0);
		} else {
			isOpen = true;
			valueAnimator = ValueAnimator.ofInt(0, measuredHeight);
		}
		
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer height = (Integer) animation.getAnimatedValue();
				
				layoutParams.height = height;
				llDesRoot.setLayoutParams(layoutParams);
			}
		});
		
		valueAnimator.addListener(new AnimatorListener() {
			
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
				// 动画结束的事件
				// 更新小箭头的方向
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
		
		valueAnimator.setDuration(200);// 动画时间
		valueAnimator.start();// 启动动画
		
	}

	@Override
	public void refreshView(AppInfo data) {
		ArrayList<SafeInfo> safe = data.safe;
		for(int i = 0; i < 4; i ++ ){
			if (i < safe.size()) {
				// 安全标识图片
				SafeInfo safeInfo = safe.get(i);
				mBitmapUtils.display(mSafeIcons[i], HttpHelper.URL
						+ "image?name=" + safeInfo.safeUrl);
				// 安全描述文字
				mSafeDes[i].setText(safeInfo.safeDes);
				// 安全描述图片
				mBitmapUtils.display(mDesIcons[i], HttpHelper.URL
						+ "image?name=" + safeInfo.safeDesUrl);
			} else {
				// 剩下不应该显示的图片
				mSafeIcons[i].setVisibility(View.GONE);

				// 隐藏多余的描述条目
				mSafeDesBar[i].setVisibility(View.GONE);
			}
		}
		
		llDesRoot.measure(0, 0);
		measuredHeight = llDesRoot.getMeasuredHeight();
		
		layoutParams = (LinearLayout.LayoutParams) llDesRoot.getLayoutParams();
		layoutParams.height = 0;
		llDesRoot.setLayoutParams(layoutParams);
	}

}
