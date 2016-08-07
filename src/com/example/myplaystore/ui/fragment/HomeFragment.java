package com.example.myplaystore.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.myplaystore.R;
import com.example.myplaystore.ui.MyBaseAdapter;
import com.example.myplaystore.ui.holder.BaseHolder;
import com.example.myplaystore.ui.holder.HomeHolder;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends BaseFragment {
	private ListView listView;
	private List<String> list;

	@Override
	public View onCreateSuccessView() {
//		TextView text = new TextView(UIUtils.getContext());
//		System.out.println("这里被调用了吗");
//		text.setText(getClass().getSimpleName());
		
		listView = new ListView(UIUtils.getContext());
		
		list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add("我是第一代的数据");
		}
		
		listView.setAdapter(new HomeAdapter(list));
		return listView;
	}

	@Override
	public ResultState onLoad() {
		return ResultState.STATE_SUCCESS;
	}
	
	
	private class HomeAdapter extends MyBaseAdapter<String>{

		public HomeAdapter(List<String> data) {
			super(data);
		}
		
		@Override
		public BaseHolder<String> getHolder() {
			return new HomeHolder();
		}
		
	}

}
