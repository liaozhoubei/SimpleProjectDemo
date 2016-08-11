package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.domain.SubjectInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class SubjectHolder extends BaseHolder<SubjectInfo> {
	private ImageView iv_pic;
	private TextView tv_title;
	private BitmapUtils bitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_subject);
		iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(SubjectInfo data) {
		bitmapUtils.display(iv_pic, HttpHelper.URL + "image?name=" + data.url);
		tv_title.setText(data.des);
		
	}

}
