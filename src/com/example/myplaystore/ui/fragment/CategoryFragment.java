package com.example.myplaystore.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.myplaystore.domain.CategoryInfo;
import com.example.myplaystore.http.protocol.CategoryProtocol;
import com.example.myplaystore.ui.MyBaseAdapter;
import com.example.myplaystore.ui.holder.BaseHolder;
import com.example.myplaystore.ui.view.MyListView;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;

public class CategoryFragment extends BaseFragment {

	private List<CategoryInfo> data;

	@Override
	public View onCreateSuccessView() {
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new CategoryAdapter(data));
		return listView;
	}

	@Override
	public ResultState onLoad() {
		CategoryProtocol categoryProtocol = new CategoryProtocol();
		data = categoryProtocol.getData(0);
		return check(data);
	}

	
	private class CategoryAdapter extends MyBaseAdapter<CategoryInfo>{

		public CategoryAdapter(List<CategoryInfo> data) {
			super(data);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseHolder<CategoryInfo> getHolder() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return super.getItemViewType(position) + 1;
		}
		
		@Override
		public int getInnerType(int position) {
			CategoryInfo categoryInfo = data.get(position);
			if (categoryInfo.isTitle){
				// 返回标题类型
				return super.getInnerType(position) + 1;
			} else {
				// 返回普通类型
				return super.getInnerType(position);
			}
			
		}
		
		// 禁用加载更多
		@Override
		public boolean hasMore() {
			return false;
		}

		@Override
		public ArrayList<CategoryInfo> onLoadMore() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
