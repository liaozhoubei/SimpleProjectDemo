package com.example.myplaystore.ui.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView{

	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MyListView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		this.setSelector(new ColorDrawable());
		this.setDivider(null);
		this.setCacheColorHint(Color.TRANSPARENT);
	}
	
	

}
