package com.example.myplaystore.ui.holder;

import android.view.View;

public abstract class BaseHolder<T> {
	
	private View mRootView;
	
	private T data;
	
	
	
	public BaseHolder() {
		super();
		mRootView = initView();
		mRootView.setTag(this);
	}

	public abstract View initView();
	
	
	public View getRootView(){
		return mRootView;
	}
	
	public void setData(T data){
		this.data = data;
		refreshView(data);
	}
	
	public T getData(){
		return data;
	}
	
	public abstract void refreshView(T data);
}
