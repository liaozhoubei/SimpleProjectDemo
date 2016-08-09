package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.utils.BitmapHelper;
import com.example.myplaystore.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AppHolder extends BaseHolder<AppInfo> {

	private ImageView iv_icon;
	private TextView tv_name;
	private RatingBar rb_star;
	private TextView tv_size;
	private TextView tv_des;
	private BitmapUtils bitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.getView(R.layout.list_item_home);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_star = (RatingBar) view.findViewById(R.id.rb_star);
		tv_size = (TextView) view.findViewById(R.id.tv_size);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
//		bitmapUtils = new BitmapUtils(UIUtils.getContext());
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		tv_name.setText(data.name);
		rb_star.setRating(data.stars);
		tv_des.setText(data.des);
		tv_size.setText(data.size + "");
		
		bitmapUtils.display(iv_icon, HttpHelper.URL + "image?name=" + data.iconUrl);
		
	}

}
