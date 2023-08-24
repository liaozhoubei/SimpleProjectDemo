package com.example.myplaystore.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.DownloadInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.http.HttpHelper.HttpResult;
import com.example.myplaystore.utils.IOUtils;
import com.example.myplaystore.utils.UIUtils;

import android.content.Intent;
import android.net.Uri;

public class DownloadManager {

	public static final int STATE_UNDO = 1;
	public static final int STATE_WAITING = 2;
	public static final int STATE_DOWNLOADING = 3;
	public static final int STATE_PAUSE = 4;
	public static final int STATE_ERROR = 5;
	public static final int STATE_SUCCESS = 6;

	private static DownloadManager downloadManager = new DownloadManager();
	private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();
	private ConcurrentHashMap<String, DownloadInfo> downloadinfos = new ConcurrentHashMap<String, DownloadInfo>();
	private ConcurrentHashMap<String, DownloadTask> downloadTasks = new ConcurrentHashMap<String, DownloadManager.DownloadTask>();

	public DownloadManager() {

	}

	public static DownloadManager getInstance() {
		return downloadManager;
	}

	public synchronized void registerObserver(DownloadObserver obsver) {
		if (obsver != null && !mObservers.contains(obsver)) {
			mObservers.add(obsver);
		}
	}

	public synchronized void unregisterObserver(DownloadObserver obsver) {
		if (obsver != null && mObservers.contains(obsver)) {
			mObservers.remove(obsver);
		}
	}

	public synchronized void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
		for (DownloadObserver downloadObsver : mObservers) {
			downloadObsver.onDownloadStateChanged(downloadInfo);
		}
	}

	public synchronized void notifyProgressChanged(DownloadInfo downloadInfo) {
		for (DownloadObserver downloadObsver : mObservers) {
			downloadObsver.onDownloadProgressChanged(downloadInfo);
		}
	}

	public synchronized void download(AppInfo info) {
		DownloadInfo downloadInfo = downloadinfos.get(info.id);
		if (downloadInfo == null) {
			downloadInfo = DownloadInfo.copy(info);
		}
		downloadInfo.currentState = STATE_WAITING;
		notifyDownloadStateChanged(downloadInfo);
		downloadinfos.put(downloadInfo.id, downloadInfo);

		DownloadTask task = new DownloadTask(downloadInfo);
		ThreadManager.getThreadPool().execute(task);
		downloadTasks.put(downloadInfo.id, task);
	}

	class DownloadTask implements Runnable {

		private DownloadInfo downloadInfo;

		public DownloadTask(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		@Override
		public void run() {
			downloadInfo.currentState = STATE_DOWNLOADING;
			notifyDownloadStateChanged(downloadInfo);
			File file = new File(downloadInfo.path);
			HttpResult httpResult;
			if (!file.exists() || file.length() != downloadInfo.currentPos || downloadInfo.currentPos == 0) {
				file.delete();
				downloadInfo.currentPos = 0;
				httpResult = HttpHelper.download(HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl);
			} else {
				// 断点下载
				httpResult = HttpHelper.download(
						HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl + "&range=" + file.length());
			}

			if (httpResult != null && httpResult.getInputStream() != null) {
				InputStream in = httpResult.getInputStream();
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file, true);
					int len = 0;
					byte[] bs = new byte[1024];
					while ((len = in.read(bs)) != -1 && downloadInfo.currentState == STATE_DOWNLOADING) {
						out.write(bs, 0, len);
						out.flush();
						downloadInfo.currentPos += len;
						notifyProgressChanged(downloadInfo);
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.close(in);
					IOUtils.close(out);
				}
				
				if (file.length() == downloadInfo.size) {
					// 文件完整, 表示下载成功
					downloadInfo.currentState = STATE_SUCCESS;
					notifyDownloadStateChanged(downloadInfo);
				} else if (downloadInfo.currentState == STATE_PAUSE) {
					// 中途暂停
					notifyDownloadStateChanged(downloadInfo);
				} else {
					// 下载失败
					file.delete();// 删除无效文件
					downloadInfo.currentState = STATE_ERROR;
					downloadInfo.currentPos = 0;
					notifyDownloadStateChanged(downloadInfo);
				}
				
			} else {
				// 网络异常
				file.delete();
				downloadInfo.currentState = STATE_ERROR;
				downloadInfo.currentPos = 0;
				notifyDownloadStateChanged(downloadInfo);
			}
			
			downloadTasks.remove(downloadInfo.id);

		}

	}

	public synchronized void pause(AppInfo info) {
		DownloadInfo downloadInfo = downloadinfos.get(info.id);
		if (downloadInfo != null) {
			if (downloadInfo.currentState == STATE_DOWNLOADING || downloadInfo.currentState == STATE_WAITING) {
				DownloadTask task = downloadTasks.get(downloadInfo.id);
				if (task != null) {
					ThreadManager.getThreadPool().cancel(task);
				}
				downloadInfo.currentState = STATE_PAUSE;
				notifyDownloadStateChanged(downloadInfo);
			}
		}
	}

	public synchronized void install(AppInfo info) {
		DownloadInfo downloadInfo = downloadinfos.get(info.id);
		if (downloadInfo != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + downloadInfo.path), "application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(intent);

		}

	}

	public interface DownloadObserver {
		public void onDownloadStateChanged(DownloadInfo downloadInfo);

		public void onDownloadProgressChanged(DownloadInfo downloadInfo);
	}

	public DownloadInfo getDownloadInfo(AppInfo info){
		return downloadinfos.get(info.id);
	}
	




}
