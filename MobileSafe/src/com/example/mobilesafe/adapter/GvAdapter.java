package com.example.mobilesafe.adapter;

import com.example.mobilesafe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GvAdapter extends BaseAdapter{
	private LayoutInflater mLayoutInflater;
	
	public GvAdapter(Context context){
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
			R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
			R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
	String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
			"高级工具", "设置中心" };

	@Override
	public int getCount() {
		return 9;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (convertView == null){
			viewholder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_main, null);
			viewholder.iv_itemmain_icon = (ImageView)convertView.findViewById(R.id.iv_itemmain_icon);
			viewholder.tv_itemmain_text = (TextView) convertView.findViewById(R.id.tv_itemmain_text);
			convertView.setTag(viewholder);
		} else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.iv_itemmain_icon.setImageResource(imageId[position]);//给imageview设置图片,根据条目的位置从图片数组中获取相应的图片
		viewholder.tv_itemmain_text.setText(names[position]);
		
		return convertView;
	}
	
	static class ViewHolder{
		ImageView iv_itemmain_icon;
		TextView tv_itemmain_text;
	}
	
}