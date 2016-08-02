package com.bproject.zhihuibeijing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class NewsDetailActivity extends Activity implements OnClickListener {
	private WebView wv_news_detail;
	private ProgressBar pb_loading;
	private LinearLayout ll_control;
	private ImageButton btn_back;
	private ImageButton btn_textsize;
	private ImageButton btn_share;
	private String mUrl;
	private int mTempWhich;// 记录临时选择的字体大小(点击确定之前)

	private int mCurrenWhich = 2;// 记录当前选中的字体大小(点击确定之后), 默认正常字体

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		mUrl = getIntent().getStringExtra("URL");
		initView();
		initData();
		setClick();
	}

	private void initView() {
		wv_news_detail = (WebView) findViewById(R.id.wv_news_detail);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
		ll_control = (LinearLayout) findViewById(R.id.ll_control);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_textsize = (ImageButton) findViewById(R.id.btn_textsize);
		ImageButton btn_menu = (ImageButton) findViewById(R.id.btn_menu);
		btn_share = (ImageButton) findViewById(R.id.btn_share);

		btn_menu.setVisibility(View.GONE);
		ll_control.setVisibility(View.VISIBLE);
		btn_back.setVisibility(View.VISIBLE);
	}

	private void initData() {
		wv_news_detail.loadUrl(mUrl);
		WebSettings settings = wv_news_detail.getSettings();
		settings.setBuiltInZoomControls(true); // 可以双击缩放
		settings.setJavaScriptEnabled(true); // 可以使用js

		wv_news_detail.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pb_loading.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				pb_loading.setVisibility(View.INVISIBLE);
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wv_news_detail.goBack();
		wv_news_detail.goForward();

		wv_news_detail.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// 获取当前网页加载进度，可用于更新进度条
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// 获得网页标题
				super.onReceivedTitle(view, title);
			}
		});
	}

	private void setClick() {
		btn_textsize.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_textsize:

			Builder builder = new AlertDialog.Builder(NewsDetailActivity.this);
			String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };
			builder.setSingleChoiceItems(items, mCurrenWhich, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mTempWhich = which;
				}

			});
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					WebSettings settings = wv_news_detail.getSettings();
					switch (mTempWhich) {
					case 0:
						// 超大字体
						settings.setTextSize(TextSize.LARGEST);
						// settings.setTextZoom(22);
						break;
					case 1:
						// 大字体
						settings.setTextSize(TextSize.LARGER);
						break;
					case 2:
						// 正常字体
						settings.setTextSize(TextSize.NORMAL);
						break;
					case 3:
						// 小字体
						settings.setTextSize(TextSize.SMALLER);
						break;
					case 4:
						// 超小字体
						settings.setTextSize(TextSize.SMALLEST);
						break;

					default:
						break;
					}
					mCurrenWhich = mTempWhich;
				}

			});
			builder.setNegativeButton("取消", null);
			builder.show();
			break;
		case R.id.btn_share:

			break;

		default:
			break;
		}
	}

}
