package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.DownloadInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.manager.DownloadManager;
import com.example.myplaystore.manager.DownloadManager.DownloadObserver;
import com.example.myplaystore.ui.view.ProgressArc;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HomeHolder extends BaseHolder<AppInfo> implements DownloadObserver, OnClickListener{

	private ImageView iv_icon;
	private TextView tv_name;
	private RatingBar rb_star;
	private TextView tv_size;
	private TextView tv_des;
	private BitmapUtils bitmapUtils;
	private FrameLayout fl_progress;
	private ProgressArc progressArc;
	private DownloadManager downloadManager;
	private int mCurrentState;
	private float mProgress;
	private TextView tv_download;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_home);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_star = (RatingBar) view.findViewById(R.id.rb_star);
		tv_size = (TextView) view.findViewById(R.id.tv_size);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
//		bitmapUtils = new BitmapUtils(UIUtils.getContext());
		bitmapUtils = BitmapHelper.getBitmapUtils();
		
		fl_progress = (FrameLayout) view.findViewById(R.id.fl_progress);
		fl_progress.setOnClickListener(this);
		tv_download = (TextView) view.findViewById(R.id.tv_download);
		progressArc = new ProgressArc(UIUtils.getContext());
		progressArc.setArcDiameter(UIUtils.dip2px(26));
		progressArc.setProgressColor(UIUtils.getColor(R.color.progress));
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(UIUtils.dip2px(27), UIUtils.dip2px(27));
		fl_progress.addView(progressArc, layoutParams);
		downloadManager = DownloadManager.getInstance();
		downloadManager.registerObserver(this);
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		tv_name.setText(data.name);
		rb_star.setRating(data.stars);
		tv_des.setText(data.des);
		tv_size.setText(data.size + "");
		bitmapUtils.display(iv_icon, HttpHelper.URL + "image?name=" + data.iconUrl);
		
		// 判断当前应用是否下载过
		DownloadInfo downloadInfo = downloadManager.getDownloadInfo(data);
		if (downloadInfo != null) {
			// 之前下载过
			mCurrentState = downloadInfo.currentState;
			mProgress = downloadInfo.getProgress();
		} else {
			// 没有下载过
			mCurrentState = DownloadManager.STATE_UNDO;
			mProgress = 0;
		}

		refreshUI(mCurrentState, mProgress, data.id);
	}
	
	private void refreshUI(int state, float progress, String id) {
		// 由于listview重用机制, 要确保刷新之前, 确实是同一个应用
		if (!getData().id.equals(id)) {
			return;
		}

		mCurrentState = state;
		mProgress = progress;
		switch (state) {
		case DownloadManager.STATE_UNDO:
			// 自定义进度条背景
			progressArc.setBackgroundResource(R.drawable.ic_download);
			// 没有进度
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tv_download.setText("下载");
			break;
		case DownloadManager.STATE_WAITING:
			progressArc.setBackgroundResource(R.drawable.ic_download);
			// 等待模式
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
			tv_download.setText("等待");
			break;
		case DownloadManager.STATE_DOWNLOADING:
			progressArc.setBackgroundResource(R.drawable.ic_pause);
			// 下载中模式
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
			progressArc.setProgress(progress, true);
			tv_download.setText((int) (progress * 100) + "%");
			break;
		case DownloadManager.STATE_PAUSE:
			progressArc.setBackgroundResource(R.drawable.ic_resume);
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			break;
		case DownloadManager.STATE_ERROR:
			progressArc.setBackgroundResource(R.drawable.ic_redownload);
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tv_download.setText("下载失败");
			break;
		case DownloadManager.STATE_SUCCESS:
			progressArc.setBackgroundResource(R.drawable.ic_install);
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			tv_download.setText("安装");
			break;

		default:
			break;
		}
	}

	// 主线程更新ui 3-4
	private void refreshUIOnMainThread(final DownloadInfo info) {
		// 判断下载对象是否是当前应用
		AppInfo appInfo = getData();
		if (appInfo.id.equals(info.id)) {
			UIUtils.runOnUIThread(new Runnable() {

				@Override
				public void run() {
					refreshUI(info.currentState, info.getProgress(), info.id);
				}
			});
		}
	}
	


	@Override
	public void onDownloadStateChanged(DownloadInfo downloadInfo) {
		refreshUIOnMainThread(downloadInfo);		
	}

	@Override
	public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
		refreshUIOnMainThread(downloadInfo);	
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_progress:
			// 根据当前状态来决定下一步操作
			if (mCurrentState == DownloadManager.STATE_UNDO
					|| mCurrentState == DownloadManager.STATE_ERROR
					|| mCurrentState == DownloadManager.STATE_PAUSE) {
				downloadManager.download(getData());// 开始下载
			} else if (mCurrentState == DownloadManager.STATE_DOWNLOADING
					|| mCurrentState == DownloadManager.STATE_WAITING) {
				downloadManager.pause(getData());// 暂停下载
			} else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
				downloadManager.install(getData());// 开始安装
			}

			break;

		default:
			break;
		}
	}

}
