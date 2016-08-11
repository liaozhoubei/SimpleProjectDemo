package com.example.myplaystore.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.myplaystore.domain.AppInfo;
import com.example.myplaystore.http.protocol.HomeProtocol;
import com.example.myplaystore.ui.MyBaseAdapter;
import com.example.myplaystore.ui.holder.BaseHolder;
import com.example.myplaystore.ui.holder.HomeHeaderHolder;
import com.example.myplaystore.ui.holder.HomeHolder;
import com.example.myplaystore.ui.view.MyListView;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.utils.UIUtils;

import android.R.color;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;

public class HomeFragment extends BaseFragment {
	private MyListView listView;
//	private List<String> list;
	private List<AppInfo> data;
	private ArrayList<String> pictures;

	@Override
	public View onCreateSuccessView() {
		
		listView = new MyListView(UIUtils.getContext());
		
//		listView.setSelector(new ColorDrawable());
//		listView.setDivider(null);
//		listView.setCacheColorHint(Color.TRANSPARENT);
		
		HomeHeaderHolder headerHolder = new HomeHeaderHolder();
		
		listView.addHeaderView(headerHolder.getRootView());
		listView.setAdapter(new HomeAdapter(data));
		if (pictures != null) {
			
			headerHolder.setData(pictures);
		}
		return listView;
	}

	@Override
	public ResultState onLoad() {
		
		HomeProtocol homeProtocol = new HomeProtocol();
		data = homeProtocol.getData(0);
		pictures = homeProtocol.getPictures();
		// 传入json
		return check(data);
	}
	
	private class HomeAdapter extends MyBaseAdapter<AppInfo>{

		public HomeAdapter(List<AppInfo> data) {
			super(data);
		}
		
		@Override
		public BaseHolder<AppInfo> getHolder(int position) {
			return new HomeHolder();
		}

		@Override
		public ArrayList<AppInfo> onLoadMore() {

			
			HomeProtocol homeProtocol = new HomeProtocol();
			ArrayList<AppInfo> moreData = homeProtocol.getData(getListSize());
			
			return moreData;
		}
		
	}
	

}
