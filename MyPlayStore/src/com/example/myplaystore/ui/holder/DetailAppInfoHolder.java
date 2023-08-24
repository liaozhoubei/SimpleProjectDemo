package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailAppInfoHolder extends BaseHolder<AppInfo> {

	private ImageView iv_icon;
	private TextView tv_name;
	private RatingBar rb_star;
	private TextView tv_download_num;
	private TextView tv_version;
	private TextView tv_date;
	private TextView tv_size;
	private BitmapUtils mBitmapUtils;
	
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_appinfo);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_star = (RatingBar) view.findViewById(R.id.rb_star);
		tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
		tv_version = (TextView) view.findViewById(R.id.tv_version);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_size = (TextView) view.findViewById(R.id.tv_size);
		
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		mBitmapUtils.display(iv_icon, HttpHelper.URL + "image?name="
				+ data.iconUrl);
		tv_name.setText(data.name);
		tv_download_num.setText("下载量:" + data.downloadNum);
		tv_version.setText("版本号:" + data.version);
		tv_date.setText(data.date);
		tv_size.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
		rb_star.setRating(data.stars);
	}

}
