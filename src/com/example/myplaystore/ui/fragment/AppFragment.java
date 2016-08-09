package com.example.myplaystore.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.http.protocol.AppProtocol;
import com.example.myplaystore.ui.MyBaseAdapter;
import com.example.myplaystore.ui.holder.AppHolder;
import com.example.myplaystore.ui.holder.BaseHolder;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.ui.view.MyListView;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;

public class AppFragment extends BaseFragment {
	private List<AppInfo> data;

	@Override
	public View onCreateSuccessView() {
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new AppAdapter(data));
		return listView;
	}

	@Override
	public ResultState onLoad() {
		
		AppProtocol appProtocol = new AppProtocol();
		data = appProtocol.getData(0);
		
		return check(data);
	}
	
	
	private class AppAdapter extends MyBaseAdapter<AppInfo>{

		public AppAdapter(List<AppInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder<AppInfo> getHolder() {
			return new AppHolder();
		}

		@Override
		public ArrayList<AppInfo> onLoadMore() {
			AppProtocol appProtocol = new AppProtocol();
			ArrayList<AppInfo> moreData = appProtocol.getData(getListSize());
			return moreData;
		}
		
	}

}
