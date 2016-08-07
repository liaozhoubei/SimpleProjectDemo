package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;
import android.widget.TextView;

public class HomeHolder extends BaseHolder<String> {

	private TextView text;

	@Override
	public View initView() {
		View view = UIUtils.getView(R.layout.list_item_home);
		text = (TextView) view.findViewById(R.id.Home_tv_text);
		return view;
	}

	@Override
	public void refreshView(String data) {
		text.setText(data);
	}

}
