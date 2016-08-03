package com.bproject.zhihuibeijing.base.imple;

import java.util.ArrayList;

import com.bproject.zhihuibeijing.MainActivity;
import com.bproject.zhihuibeijing.base.BaseMenuDetailPager;
import com.bproject.zhihuibeijing.base.BasePager;
import com.bproject.zhihuibeijing.base.imple.menu.InteractMenuDetailPager;
import com.bproject.zhihuibeijing.base.imple.menu.NewsMenuDetailPager;
import com.bproject.zhihuibeijing.base.imple.menu.PhotosMenuDetailPager;
import com.bproject.zhihuibeijing.base.imple.menu.TopicMenuDetailPager;
import com.bproject.zhihuibeijing.domain.NewsMenu;
import com.bproject.zhihuibeijing.fragment.LeftMenuFragment;
import com.bproject.zhihuibeijing.global.GolbalConstants;
import com.bproject.zhihuibeijing.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewsCenterPager extends BasePager {
	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// 菜单详情页集合
	private NewsMenu mNewsData;
	

	public NewsCenterPager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		
		TextView textview = new TextView(mActivity);
		textview.setText("新闻中心");
		textview.setTextSize(18);
		textview.setTextColor(Color.RED);
		textview.setGravity(Gravity.CENTER);
		flContent.addView(textview);
		
		
		tvTitle.setText("新闻中心");
		
		String cache = CacheUtils.getCache(GolbalConstants.CATEGORY_URL, mActivity);
		if (!TextUtils.isEmpty(cache)){
			processData(cache);
		} 
		getDataFromServer();
		
		
		
	}
	
	private void getDataFromServer(){
		HttpUtils httpUtils = new HttpUtils();
		/**
		 * 在android 6.0（API 23）中，Google已经移除了移除了Apache HttpClient相关的类
			推荐使用HttpUrlConnection，如果要继续使用需要Apache HttpClient，需要在eclipse下libs里添加org.apache.http.legacy.jar
		 */
		httpUtils.send(HttpMethod.GET, GolbalConstants.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println(result);
				processData(result);
				CacheUtils.setCache(GolbalConstants.CATEGORY_URL, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		} );
	}
	
	protected void processData(String json){
		Gson gson = new Gson();
		mNewsData = gson.fromJson(json, NewsMenu.class);
		System.out.println("解析结果"+ mNewsData);
		
		MainActivity mainActivity = (MainActivity) mActivity;
		LeftMenuFragment leftFragment = mainActivity.getLeftFragment();
		leftFragment.setMenuData(mNewsData.data);
		
		mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity, btn_photo));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
		
		setCurrentDetailPager(0);
	}

	public void setCurrentDetailPager(int position) {
		BaseMenuDetailPager baseMenuDetailPager = mMenuDetailPagers.get(position);
		View view = baseMenuDetailPager.mRootView;
		
		flContent.removeAllViews();
		baseMenuDetailPager.initData();
		flContent.addView(view);
		tvTitle.setText(mNewsData.data.get(position).title);
		
		// 判断当前页面属于哪个页面，如果属于组图页面这显示切换图片形式按钮
		if (baseMenuDetailPager instanceof PhotosMenuDetailPager){
			btn_photo.setVisibility(View.VISIBLE);
		} else {
			btn_photo.setVisibility(View.GONE);
		}
	}

}
