package com.example.myplaystore.ui;

import java.util.List;

import com.example.myplaystore.ui.holder.BaseHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	
	private List<T> data;
	
	public MyBaseAdapter(List<T> data) {
		super();
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder baseHolder;
		if(convertView == null) {
			baseHolder = getHolder();
			convertView = baseHolder.getView();
		} else {
			baseHolder = (BaseHolder) convertView.getTag();
		}
		
		baseHolder.setData(data.get(position));
		
		return convertView;
	}
	
	
	public abstract BaseHolder<T> getHolder();

}
