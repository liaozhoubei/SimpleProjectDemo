package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.DownloadInfo;
import com.example.myplaystore.manager.DownloadManager;
import com.example.myplaystore.manager.DownloadManager.DownloadObserver;
import com.example.myplaystore.ui.view.ProgressHorizontal;
import com.example.myplaystore.utils.UIUtils;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class DetailDownloadHolder extends BaseHolder<AppInfo> implements DownloadObserver, OnClickListener{

	private Button btn_download;
	private ProgressHorizontal pbProgress;
	private DownloadManager downloadManager;
	private int mCurrentState;
	private float mProgress;
	private FrameLayout fl_progress;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_download);
		btn_download = (Button) view.findViewById(R.id.btn_download);
		btn_download.setOnClickListener(this);
		
		
		fl_progress = (FrameLayout) view.findViewById(R.id.fl_progress);
		fl_progress.setOnClickListener(this);
		pbProgress = new ProgressHorizontal(UIUtils.getContext());
		pbProgress.setProgressBackgroundResource(R.drawable.progress_bg);
		pbProgress.setProgressResource(R.drawable.progress_normal);
		pbProgress.setProgressTextColor(Color.WHITE);
		pbProgress.setProgressTextSize(UIUtils.dip2px(16));
		
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		
		fl_progress.addView(pbProgress, layoutParams);
		
		downloadManager = DownloadManager.getInstance();
		downloadManager.registerObserver(this);
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		DownloadInfo downloadInfo = downloadManager.getDownloadInfo(data);
		if (downloadInfo != null) {
			mCurrentState = downloadInfo.currentState;
			mProgress = downloadInfo.getProgress();
		} else {
			mCurrentState = DownloadManager.STATE_UNDO;
			mProgress = 0;
		}
		refreshUI(mCurrentState, mProgress);
	}

	private void refreshUI(int currentState, float progress) {

		//System.out.println("刷新ui了:" + currentState);

		mCurrentState = currentState;
		mProgress = progress;

		switch (currentState) {
		case DownloadManager.STATE_UNDO:// 未下载
			fl_progress.setVisibility(View.GONE);
			btn_download.setVisibility(View.VISIBLE);
			btn_download.setText("下载");
			break;

		case DownloadManager.STATE_WAITING:// 等待下载
			fl_progress.setVisibility(View.GONE);
			btn_download.setVisibility(View.VISIBLE);
			btn_download.setText("等待中..");
			break;

		case DownloadManager.STATE_DOWNLOADING:// 正在下载
			fl_progress.setVisibility(View.VISIBLE);
			btn_download.setVisibility(View.GONE);
			pbProgress.setCenterText("");
			pbProgress.setProgress(mProgress);// 设置下载进度
			break;

		case DownloadManager.STATE_PAUSE:// 下载暂停
			fl_progress.setVisibility(View.VISIBLE);
			btn_download.setVisibility(View.GONE);
			pbProgress.setCenterText("暂停");
			pbProgress.setProgress(mProgress);

			System.out.println("暂停界面更新:" + mCurrentState);
			break;

		case DownloadManager.STATE_ERROR:// 下载失败
			fl_progress.setVisibility(View.GONE);
			btn_download.setVisibility(View.VISIBLE);
			btn_download.setText("下载失败");
			break;

		case DownloadManager.STATE_SUCCESS:// 下载成功
			fl_progress.setVisibility(View.GONE);
			btn_download.setVisibility(View.VISIBLE);
			btn_download.setText("安装");
			break;

		default:
			break;
		}

	}
	
	private void refreshUIOnMainThread(final DownloadInfo info){
		AppInfo appInfo = getData();
		if (appInfo.id.equals(info.id)){
			UIUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					refreshUI(info.currentState, info.getProgress());	
					System.out.println("下载进度:" + info.currentPos);
				}
			});
			
		}
	}


	
	@Override
	public void onClick(View v) {
		//System.out.println("点击事件响应了:" + mCurrentState);

		switch (v.getId()) {
		case R.id.btn_download:
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

	@Override
	public void onDownloadStateChanged(DownloadInfo info) {
		refreshUIOnMainThread(info);		
	}

	@Override
	public void onDownloadProgressChanged(DownloadInfo info) {
		refreshUIOnMainThread(info);	
	}


}
