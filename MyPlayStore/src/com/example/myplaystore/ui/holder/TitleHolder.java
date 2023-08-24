package com.example.myplaystore.ui.holder;

import com.example.myplaystore.R;
import com.example.myplaystore.domain.CategoryInfo;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;
import android.widget.TextView;

public class TitleHolder extends BaseHolder<CategoryInfo>{

	private TextView tv_title;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_title);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		tv_title.setText(data.title);
	}

}
