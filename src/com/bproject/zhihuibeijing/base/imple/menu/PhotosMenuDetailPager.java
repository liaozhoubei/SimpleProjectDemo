package com.bproject.zhihuibeijing.base.imple.menu;

import java.util.ArrayList;

import com.bproject.zhihuibeijing.R;
import com.bproject.zhihuibeijing.base.BaseMenuDetailPager;
import com.bproject.zhihuibeijing.domain.PhotoBean;
import com.bproject.zhihuibeijing.domain.PhotoBean.PhotoNews;
import com.bproject.zhihuibeijing.global.GolbalConstants;
import com.bproject.zhihuibeijing.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotosMenuDetailPager extends BaseMenuDetailPager implements OnClickListener{

	private ListView lv_photo;
	private GridView gv_photo;
	private ArrayList<PhotoNews> photoNews;
	private PhotoAdapter photoAdapter;
	private ImageButton btn_photo;

	public PhotosMenuDetailPager(Activity mActivity, ImageButton btn_photo) {
		super(mActivity);
		btn_photo.setOnClickListener(this);
		this.btn_photo = btn_photo;
	}


	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		lv_photo = (ListView) view.findViewById(R.id.lv_photo);
		gv_photo = (GridView) view.findViewById(R.id.gv_photo);
		
		return view;
	}
	
	@Override
	public void initData() {
		String cache = CacheUtils.getCache(GolbalConstants.PHOTOS_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		getDataFromServer();
		
		
	}
	
	
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GolbalConstants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String json = responseInfo.result;
				processData(json);
				CacheUtils.setCache(GolbalConstants.PHOTOS_URL, json, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}


	protected void processData(String json) {
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(json, PhotoBean.class);
		photoNews = photoBean.data.news;
		
		for (int i = 0; i < photoNews.size(); i++) {
			System.out.println("这个是好图片" + photoNews.get(i).listimage);
		}
		photoAdapter = new PhotoAdapter();
		lv_photo.setAdapter(photoAdapter);
		gv_photo.setAdapter(photoAdapter);
	}


	private class PhotoAdapter extends BaseAdapter{
		private BitmapUtils bitmapUtils;
		
		public PhotoAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return photoNews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return photoNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
				viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tv_title.setText(photoNews.get(position).title);
			bitmapUtils.display(viewHolder.iv_pic, photoNews.get(position).listimage);
			
			return convertView;
		}
		
	}
	static class ViewHolder{
		ImageView iv_pic;
		TextView tv_title;
	}
	
	private boolean isListView = true;
	@Override
	public void onClick(View v) {
		if (isListView) {
			lv_photo.setVisibility(View.GONE);
			gv_photo.setVisibility(View.VISIBLE);
			btn_photo.setImageResource(R.drawable.icon_pic_list_type);
			isListView = false;
		} else {
			lv_photo.setVisibility(View.VISIBLE);
			gv_photo.setVisibility(View.GONE);
			btn_photo.setImageResource(R.drawable.icon_pic_grid_type);
			isListView = true;
		}
	}

}
