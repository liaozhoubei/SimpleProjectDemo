package com.example.myplaystore.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.myplaystore.ui.holder.BaseHolder;
import com.example.myplaystore.ui.holder.MoreHolder;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	private static final int TYPE_NORMAL = 0;
	private static final int TYPE_MORE = 1;

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
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == data.size() - 1) {
			return TYPE_MORE;
		} else {
			return getInnerType();
		}
	}

	// 公开的方法，方便以后让子类可重写此方法更改返回的类型
	public int getInnerType() {
		return TYPE_NORMAL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder baseHolder;
		if (convertView == null) {

			if (getItemViewType(position) == TYPE_MORE) {
				baseHolder = new MoreHolder(hasMore());
			} else {
				baseHolder = getHolder();
			}
		} else {
			baseHolder = (BaseHolder) convertView.getTag();
		}

		if (getItemViewType(position) != TYPE_MORE) {
			baseHolder.setData(data.get(position));
		} else {
			MoreHolder moreHolder = (MoreHolder) baseHolder;
			if (moreHolder.getData() == MoreHolder.STATE_MORE_MORE){
				
				LoadMore(moreHolder);
			}
		}

		return baseHolder.getRootView();
	}

	public boolean hasMore() {
		return true;
	}

	public abstract BaseHolder<T> getHolder();

	private boolean isLoading = false;

	public void LoadMore(final MoreHolder moreholder) {
		if (!isLoading) {
			isLoading = true;
			new Thread() {
				
				
				@Override
				public void run() {
					super.run();
					final ArrayList<T> moredata = onLoadMore();
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							
							if (moredata != null) {
								if (moredata.size() < 20) {
									moreholder.setData(MoreHolder.STATE_MORE_NONE);
									Toast.makeText(UIUtils.getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
								} else {
									moreholder.setData(MoreHolder.STATE_MORE_MORE);
								}
								data.addAll(moredata);
								MyBaseAdapter.this.notifyDataSetChanged();
								
							} else {
								moreholder.setData(MoreHolder.STATE_MORE_ERROR);
							}
							isLoading = false;
						}
					});
				}
			}.start();
		}
	}

	public abstract ArrayList<T> onLoadMore();

	public int getListSize(){
		return data.size();
	}
}
