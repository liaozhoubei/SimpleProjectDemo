package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.TaskInfo;
import com.example.mobilesafe.engine.TaskEngine;
import com.example.mobilesafe.utils.MyAsycnTaks;
import com.example.mobilesafe.utils.TaskUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskManagerActivity extends Activity {
	private ListView mLv_taskmanager_processes;
	private ProgressBar mLoading;
	private List<TaskInfo> mList;
	private List<TaskInfo> mUserInfos;
	private List<TaskInfo> mSystemInfos;
	private TaskInfoAdapter mTaskInfoAdapter;
	private TaskInfo taskinfo;
	private TextView tv_taskmanager_freeandtotalram;
	private TextView tv_taskmanager_processes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskmanager);
		mLv_taskmanager_processes = (ListView) findViewById(R.id.lv_taskmanager_processes);
		mLoading = (ProgressBar) findViewById(R.id.loading);
		tv_taskmanager_processes = (TextView)findViewById(R.id.tv_taskmanager_processes);
		tv_taskmanager_freeandtotalram = (TextView)findViewById(R.id.tv_taskmanager_processes);
		int processCount = TaskUtil.getProcessCount(getApplicationContext());
		tv_taskmanager_processes.setText("正在运行的程序个数：" + processCount);
		
		long availableRam = TaskUtil.getAvailableRam(getApplicationContext());
		String RamNow = Formatter.formatFileSize(getApplicationContext(), availableRam);
		int version = android.os.Build.VERSION.SDK_INT;
		long totalRam;
		if (version >= 16){
			totalRam = TaskUtil.getTotalRam(getApplicationContext());
			
		} else {
			totalRam = TaskUtil.getTotalRam();
		}
		String allRam = Formatter.formatFileSize(getApplicationContext(), totalRam);
		tv_taskmanager_freeandtotalram.setText("可用内存\\总内存：" + RamNow + "\\" + allRam);
		filldata();
		listViewItemClick();
	}

	private void listViewItemClick() {
		mLv_taskmanager_processes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position <= mUserInfos.size()){
					taskinfo = mUserInfos.get(position - 1);
				} else {
					taskinfo = mSystemInfos.get(position - mUserInfos.size() - 2);
				}
				
				if (taskinfo.isChecked()) {
					taskinfo.setChecked(false);
				} else {
					if (!taskinfo.getPackageName().equals(getPackageName())){
						taskinfo.setChecked(true);
					}
					
				}
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.cb_itemtaskmanager_ischecked.setChecked(taskinfo.isChecked());
			}
		});		
	}

	private void filldata() {
		new MyAsycnTaks() {
			
			@Override
			public void preTask() {
				mLoading.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void postTast() {
				if (mTaskInfoAdapter == null) {
					mTaskInfoAdapter = new TaskInfoAdapter();
					mLv_taskmanager_processes.setAdapter(mTaskInfoAdapter);
				} else {
					mTaskInfoAdapter.notifyDataSetChanged();
				}
				
				mLoading.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void doingTast() {
				mList = TaskEngine.getTaskAllInfo(getApplicationContext());
				mUserInfos = new ArrayList<TaskInfo>();
				mSystemInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : mList) {
					if (taskInfo.isUser()) {
						mUserInfos.add(taskInfo);
					} else {
						mSystemInfos.add(taskInfo);
					}
				}
			}
		}.excute();
	}
	
	private class TaskInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mUserInfos.size() + mSystemInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viewHolder;
			
			if (position == 0) {
				TextView text = new TextView(getApplicationContext());
				text.setText("用户进程(" +  mUserInfos.size() + ")");
				text.setTextColor(Color.WHITE);
				text.setBackgroundColor(Color.GRAY);
				return text;
			} else  if (position  == mUserInfos.size()+1){
				TextView text = new TextView(getApplicationContext());
				text.setText("系统进程(" +  mSystemInfos.size() + ")");
				text.setTextColor(Color.WHITE);
				text.setBackgroundColor(Color.GRAY);
				return text;
			}
			
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(), R.layout.item_taskmanager, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_itemtaskmanager_icon = (ImageView) view.findViewById(R.id.iv_itemtaskmanager_icon);
				viewHolder.tv_itemtaskmanager_name = (TextView) view.findViewById(R.id.tv_itemtaskmanager_name);
				viewHolder.tv_itemtaskmanager_ram = (TextView) view.findViewById(R.id.tv_itemtaskmanager_ram);
				viewHolder.cb_itemtaskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_itemtaskmanager_ischecked);
				viewHolder.cb_itemtaskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_itemtaskmanager_ischecked);
				view.setTag(viewHolder);
			}
			TaskInfo info;
			if (position <= mUserInfos.size()){
				info = mUserInfos.get(position - 1);
			} else {
				info = mSystemInfos.get(position - mUserInfos.size() - 2);
			}
			
			
			
			//设置显示数据,null.方法    参数为null 
			if (info.getIcon() == null) {
				viewHolder.iv_itemtaskmanager_icon.setImageResource(R.drawable.ic_default);
			}else{
				viewHolder.iv_itemtaskmanager_icon.setImageDrawable(info.getIcon());
			}
			
			//名称为空的可以设置为包名
			if (TextUtils.isEmpty(info.getName())) {
				viewHolder.tv_itemtaskmanager_name.setText(info.getPackageName());
			}else{
				viewHolder.tv_itemtaskmanager_name.setText(info.getName());
			}
			long romSize = info.getRomSize();
			String formatFileSize = Formatter.formatFileSize(getApplicationContext(), romSize);
			viewHolder.tv_itemtaskmanager_ram.setText("占用内存:" + formatFileSize);
			
			//因为checkbox的状态会跟着一起复用,所以一般要动态修改的控件的状态,不会跟着去复用,而是将状态保存到bean对象,在每次复用使用控件的时候
			//根据每个条目对应的bean对象保存的状态,来设置控件显示的相应的状态
			if (info.isChecked()) {
				viewHolder.cb_itemtaskmanager_ischecked.setChecked(true);
			}else{
				viewHolder.cb_itemtaskmanager_ischecked.setChecked(false);
			}
			//判断如果是我们的应用程序,就把checkbox隐藏,不是的话显示,在getview中有if必须有else
			if (info.getPackageName().equals(getPackageName())) {
				viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.VISIBLE);
			}
			
			
			return view;
		}
	}
	static class ViewHolder{
		ImageView iv_itemtaskmanager_icon;
		TextView tv_itemtaskmanager_name;
		TextView tv_itemtaskmanager_ram;
		CheckBox cb_itemtaskmanager_ischecked;
	}
	
	/**
	 * 全选
	 * @param v
	 */
	public void all(View v){
		for (int i = 0; i < mUserInfos.size(); i ++) {
			if ( !mUserInfos.get(i).getPackageName().equals(getPackageName())){
				mUserInfos.get(i).setChecked(true);
			}
			
		}
		for (int i = 0; i < mSystemInfos.size(); i++) {
			mSystemInfos.get(i).setChecked(true);
		}
		mTaskInfoAdapter.notifyDataSetChanged();
	}
	/**
	 * 取消
	 * @param v
	 */
	public void cancel(View v){
		for (int i = 0; i < mUserInfos.size(); i ++) {
			mUserInfos.get(i).setChecked(false);
		}
		for (int i = 0; i < mSystemInfos.size(); i++) {
			mSystemInfos.get(i).setChecked(false);
		}
		mTaskInfoAdapter.notifyDataSetChanged();
	}
	/**
	 * 清理
	 * @param v
	 */
	public void clear(View v){
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> deleteTaskInfo = new ArrayList<TaskInfo>();
		for (int i = 0; i < mUserInfos.size(); i ++) {
			if (mUserInfos.get(i).isChecked()){
				activityManager.killBackgroundProcesses(mUserInfos.get(i).getPackageName());
				deleteTaskInfo.add(mUserInfos.get(i));
			}
		}
		for (int i = 0; i < mSystemInfos.size(); i++) {
			if (mSystemInfos.get(i).isChecked()){
				activityManager.killBackgroundProcesses(mSystemInfos.get(i).getPackageName());
				deleteTaskInfo.add(mSystemInfos.get(i));
			}
		}
		int memory = 0;
		for (TaskInfo info : deleteTaskInfo){
			if (info.isUser()){
				mUserInfos.remove(info);
			} else {
				mSystemInfos.remove(info);
			}
			memory += info.getRomSize();
		}
		String formatFileSize = Formatter.formatFileSize(getApplicationContext(), memory);
		Toast.makeText(getApplicationContext(), "共清理"+deleteTaskInfo.size()+"个进程,释放"+formatFileSize+"内存空间", 0).show();
		deleteTaskInfo.clear();
		deleteTaskInfo = null;
		mTaskInfoAdapter.notifyDataSetChanged();
	}
	/**
	 * 设置
	 * @param v
	 */
	public void setting(View v){
		
	}
}
