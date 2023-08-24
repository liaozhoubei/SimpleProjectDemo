package com.example.myplaystore.ui.fragment;

import com.example.myplaystore.ui.MyBaseAdapter;
import com.example.myplaystore.ui.holder.BaseHolder;
import com.example.myplaystore.ui.holder.SubjectHolder;
import com.example.myplaystore.ui.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import com.example.myplaystore.domain.SubjectInfo;
import com.example.myplaystore.http.protocol.SubjectProtocol;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.utils.UIUtils;

import android.view.View;

public class SubjectFragment extends BaseFragment {
	private List<SubjectInfo> data;
	@Override
	public View onCreateSuccessView() {
		MyListView listView = new MyListView(UIUtils.getContext());
		listView.setAdapter(new SubjcetAdapter(data));
		return listView;
	}

	@Override
	public ResultState onLoad() {
		SubjectProtocol protocol = new SubjectProtocol();
		data = protocol.getData(0);
		return check(data);
	}
	
	private class SubjcetAdapter extends MyBaseAdapter<SubjectInfo>{

		public SubjcetAdapter(List<SubjectInfo> data) {
			super(data);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseHolder<SubjectInfo> getHolder(int position) {
			return new SubjectHolder();
		}

		@Override
		public ArrayList<SubjectInfo> onLoadMore() {
			SubjectProtocol protocol = new SubjectProtocol();
			ArrayList<SubjectInfo> moreDate = protocol.getData(getListSize());
			return moreDate;
		}
		
	}
}
