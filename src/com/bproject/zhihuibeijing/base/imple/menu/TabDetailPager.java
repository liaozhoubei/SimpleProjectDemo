package com.bproject.zhihuibeijing.base.imple.menu;

import java.util.ArrayList;

import com.bproject.zhihuibeijing.NewsDetailActivity;
import com.bproject.zhihuibeijing.R;
import com.bproject.zhihuibeijing.base.BaseMenuDetailPager;
import com.bproject.zhihuibeijing.domain.NewsMenu.NewsTabData;
import com.bproject.zhihuibeijing.domain.NewsTabBean;
import com.bproject.zhihuibeijing.domain.NewsTabBean.NewsData;
import com.bproject.zhihuibeijing.domain.NewsTabBean.TopNews;
import com.bproject.zhihuibeijing.global.GolbalConstants;
import com.bproject.zhihuibeijing.utils.CacheUtils;
import com.bproject.zhihuibeijing.utils.PrefUtils;
import com.bproject.zhihuibeijing.view.PullToRefreshListView;
import com.bproject.zhihuibeijing.view.PullToRefreshListView.OnRefreshListener;
import com.bproject.zhihuibeijing.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class TabDetailPager extends BaseMenuDetailPager {

	private NewsTabData mNewsTabData;
	private View view;
	private String mUrl;
	private ArrayList<TopNews> mTopNews;
	private TopNewsViewPager mViewPager;
	private TextView tv_title;
	private CirclePageIndicator indicator;
	private PullToRefreshListView lv_list;
	private ArrayList<NewsData> mNewsList;
	private NewsAdapter newsAdapter;
	private String mMoreUrl;

	public TabDetailPager(Activity mActivity, NewsTabData newsTabData) {
		super(mActivity);
		mNewsTabData = newsTabData;

		mUrl = GolbalConstants.SERVER_URL + mNewsTabData.url;
		System.out.println("输出的地址是" + mUrl);
	}

	@Override
	public View initView() {
		// view = new TextView(mActivity);
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);
		view = View.inflate(mActivity, R.layout.pager_tab_detail, null);

		lv_list = (PullToRefreshListView) view.findViewById(R.id.lv_tab_news_lsit);

		View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
		mViewPager = (TopNewsViewPager) mHeaderView.findViewById(R.id.vp_top_news);
		tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
		indicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.indicator);

		lv_list.addHeaderView(mHeaderView);

		lv_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
					lv_list.onRefreshComplete(false);
				}
			}

		});
		
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int headerViewsCount = lv_list.getHeaderViewsCount();
				position = position - headerViewsCount;
				
				NewsData newsData = mNewsList.get(position);
				String readIds = PrefUtils.getString(mActivity, "read_ids", "");
				
				if (!readIds.contains(newsData.id + "")){
					readIds = readIds + newsData.id + ",";
					PrefUtils.setString(mActivity, "read_ids", readIds);
				}
				
				TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
				tv_title.setTextColor(Color.GRAY);
				
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("URL", newsData.url);
				mActivity.startActivity(intent);
			}
		});
		
		return view;
	}

	@Override
	public void initData() {

		String Cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(Cache)) {
			processData(Cache, false);
		}

		getDataFromServer();
	}

	private void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result, true);
				 lv_list.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				 lv_list.onRefreshComplete(false);
			}
		});
	}

	private void getDataFromServer() {
		// gson.fromJson(json, classOfT)
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				CacheUtils.setCache(mUrl, result, mActivity);
				processData(result, false);
				lv_list.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				lv_list.onRefreshComplete(false);
			}
		});
	}

	private void processData(String result, boolean isMore) {
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);

		String moreUrl = newsTabBean.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			mMoreUrl = GolbalConstants.SERVER_URL + moreUrl;
		} else {
			mMoreUrl = null;
		}

		if (!isMore) {
			mTopNews = newsTabBean.data.topnews;

			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				indicator.setViewPager(mViewPager);
				indicator.setSnap(true);
				indicator.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						TopNews topNews = mTopNews.get(position);
						tv_title.setText(topNews.title);

					}

					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						// TODO Auto-generated method stub

					}
				});
			}

			TopNews topNews = mTopNews.get(0);
			tv_title.setText(topNews.title);
			indicator.onPageSelected(0);
			// 加载列表新闻

			mNewsList = newsTabBean.data.news;
			if (mNewsList != null) {
				newsAdapter = new NewsAdapter();
				lv_list.setAdapter(newsAdapter);

				// System.out.println("这个是新闻列表" + mNewsList.get(0).listimage);
			}
		} else {
			ArrayList<NewsData> more = newsTabBean.data.news;
			mNewsList.addAll(more);
			newsAdapter.notifyDataSetChanged();
		}

	}

	// Viewpager 的adapter
	private class TopNewsAdapter extends PagerAdapter {
		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			// imageView.setImageResource(R.drawable.topnews_item_default);
			imageView.setScaleType(ScaleType.FIT_XY);
			String topimageUrl = mTopNews.get(position).topimage;
			mBitmapUtils.display(imageView, topimageUrl);
			container.addView(imageView);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	private class NewsAdapter extends BaseAdapter {
		private BitmapUtils mBitmapUtils;

		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public Object getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			NewsData news = mNewsList.get(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);
			
			String readIds = PrefUtils.getString(mActivity, "read_ids", "");
			if (readIds.contains(news.id + "")){
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			
			mBitmapUtils.display(holder.ivIcon, news.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}
}
