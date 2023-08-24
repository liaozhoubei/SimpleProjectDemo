package com.example.myplaystore.ui.holder;

import java.util.ArrayList;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;

public class DetailPicsHolder extends BaseHolder<AppInfo> {
	
	private ImageView mPictures[];
	
	private BitmapUtils mBitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_picinfo);
		mPictures = new ImageView[5];
		mPictures[0] = (ImageView) view.findViewById(R.id.iv_pic1); 
		mPictures[1] = (ImageView) view.findViewById(R.id.iv_pic2); 
		mPictures[2] = (ImageView) view.findViewById(R.id.iv_pic3); 
		mPictures[3] = (ImageView) view.findViewById(R.id.iv_pic4); 
		mPictures[4] = (ImageView) view.findViewById(R.id.iv_pic5);
		
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
//		ArrayList<String> screen = data.screen;
//		for (int i = 0; i < 5; i ++) {
//			if (i < screen.size()) {
//				mBitmapUtils.display(mPictures[i], HttpHelper.URL + "image?name="
//						+ screen.get(i));
//			} else {
//				mPictures[i].setVisibility(View.GONE);
//			}
//		}
		
		final ArrayList<String> screen = data.screen;

		for (int i = 0; i < 5; i++) {
			if (i < screen.size()) {
				mBitmapUtils.display(mPictures[i], HttpHelper.URL + "image?name="
						+ screen.get(i));
				
//				ivPics[i].setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						//跳转activity, activity展示viewpager
//						//将集合通过intent传递过去, 当前点击的位置i也可以传过去
//						Intent intent = new Intent();
//						intent.putExtra("list", screen);
//					}
//				});
			} else {
				mPictures[i].setVisibility(View.GONE);
			}
		}
	}

}
