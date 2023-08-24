package com.bproject.zhihuibeijing;

import com.bproject.zhihuibeijing.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * app的启动界面
 * 
 * @author ASUS-H61M
 *
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ImageView iv_splash_horse = (ImageView) findViewById(R.id.iv_splash_horse);
		// 旋转动画
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		// 缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		// 透明度动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);

		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);

		iv_splash_horse.startAnimation(animationSet);

		// 监听app开启界面动画
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 动画完成后判断是否第一次进入，不是第一次就直接进入主界面，否则进入引导页面
				Intent intent;
				if (PrefUtils.getBoolean(SplashActivity.this, "is_First_enter", true)) {
					intent = new Intent(SplashActivity.this, GuideActivity.class);
				} else {
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});

	}

}
