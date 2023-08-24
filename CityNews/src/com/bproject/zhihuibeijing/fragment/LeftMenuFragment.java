package com.bproject.zhihuibeijing.fragment;

import java.util.ArrayList;

import com.bproject.zhihuibeijing.MainActivity;
import com.bproject.zhihuibeijing.R;
import com.bproject.zhihuibeijing.base.imple.NewsCenterPager;
import com.bproject.zhihuibeijing.domain.NewsMenu.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends BaseFragment {
	private ArrayList<NewsMenuData> mNewsMenuData;
	private ListView listview;
	private int mCurrentPos;
	private LeftMenuAdapter mAdapter;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		listview = (ListView) view.findViewById(R.id.lv_list);
		
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
	
	public void setMenuData(ArrayList<NewsMenuData> data){
		mCurrentPos = 0;//当前选中的位置归零
		
		mNewsMenuData = data;
		mAdapter = new LeftMenuAdapter();
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCurrentPos = position;
				mAdapter.notifyDataSetChanged();
				// 开关侧滑
				toggle();
				// 更新页面内容
				setCurrentDetailPager(position);
			}




		});
	}
	/**
	 * 点击侧边栏菜单时设置页面
	 * @param position
	 */
	private void setCurrentDetailPager(int position) {
		MainActivity mainActivity = (MainActivity) mActivity;
		ContentFragment contentFragment = mainActivity.getContentFragment();
		NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
		newsCenterPager.setCurrentDetailPager(position);
	}
	
	private void toggle() {
		MainActivity mainActivity = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		slidingMenu.toggle();
	}
	
	private class LeftMenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tv_menu = (TextView) view.findViewById(R.id.tv_menu);
			NewsMenuData item = getItem(position);
			tv_menu.setText(item.title);
			
			if (position == mCurrentPos) {
				tv_menu.setEnabled(true);
			} else {
				tv_menu.setEnabled(false);
			}
			
			return view;
			
		}
		
	}

}
