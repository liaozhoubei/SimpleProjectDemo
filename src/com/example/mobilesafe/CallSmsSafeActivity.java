package com.example.mobilesafe;

import java.util.List;

import com.example.mobilesafe.bean.BlackNumInfo;
import com.example.mobilesafe.dao.BlackNumDao;
import com.example.mobilesafe.utils.MyAsycnTaks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
	private List<BlackNumInfo> list;
	private ProgressBar loading;
	private ListView lv_callsmssafe_blacknums;
	private BlackNumDao mBlackNumDao;
	private BlackNumAdapter blackNumAdapter;
	private AlertDialog dialog;
	// 查询数据库是默认的起始位置
	private int startIndex = 0;
	// 一次查询数据库的数量
	private final int MAXNUM = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsmssafe);
		mBlackNumDao = new BlackNumDao(getApplicationContext());
		loading = (ProgressBar) findViewById(R.id.loading);
		
		lv_callsmssafe_blacknums = (ListView) findViewById(R.id.lv_callsmssafe_blacknums);
		fillData();
		lv_callsmssafe_blacknums.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					int position = lv_callsmssafe_blacknums.getLastVisiblePosition();
					
					if (position == list.size() - 1) {
						startIndex += MAXNUM;
						fillData();
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	
	}
	
	// 添加黑名单的点击事件
	public void addBlackNum(View v) {
		AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknum, null);
		final EditText et_addblacknum_blacknum = (EditText) view.findViewById(R.id.et_addblacknum_blacknum);
		final RadioGroup rg_addblacknum_modes = (RadioGroup) view.findViewById(R.id.rg_addblacknum_modes);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 输入号码
				String blacknum = et_addblacknum_blacknum.getText().toString().trim();
				if (TextUtils.isEmpty(blacknum)) {
					Toast.makeText(getApplicationContext(), "请输入黑名单号码", Toast.LENGTH_SHORT).show();
					return;
				}
				int mode = -1;
				int radioButtonId = rg_addblacknum_modes.getCheckedRadioButtonId();
				switch (radioButtonId) {
				case R.id.rb_addblacknum_tel:
					//电话拦截
					mode = BlackNumDao.CALL;
					break;
				case R.id.rb_addblacknum_sms:
					//短信拦截
					mode = BlackNumDao.SMS;
					break;
				case R.id.rb_addblacknum_all:
					//全部拦截
					mode = BlackNumDao.ALL;
					break;
				default:
					break;
				}
				mBlackNumDao.addBlackNum(blacknum, mode);
				list.add(0, new BlackNumInfo(blacknum, mode));
//				list.add(new BlackNumInfo(num, mode));
				blackNumAdapter.notifyDataSetChanged();
				// 选择模式
				// 加入数据库
				// 更新列表
				dialog.dismiss();
			}
		});
		btn_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
		
		System.out.println("可以添加黑名单吗？");
	}
	
	// 通过异步加载的方法获得黑名单
	private void fillData() {
		new MyAsycnTaks() {
			
			@Override
			public void preTask() {
				loading.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void postTast() {
				// 但数据发生改变时，如果为空那就设置适配器，如果不为空那就刷新适配器
				if (blackNumAdapter == null) {
					blackNumAdapter = new BlackNumAdapter();
					lv_callsmssafe_blacknums.setAdapter(blackNumAdapter);
				} else {
					blackNumAdapter.notifyDataSetChanged();
				}
				
				loading.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void doingTast() {
				if (list == null) {
					list = mBlackNumDao.queryPartBlackNumInfo(startIndex, MAXNUM);
				} else {
					list.addAll(mBlackNumDao.queryPartBlackNumInfo(startIndex, MAXNUM));
				}
				
			}
		}.excute(); ;
	}

	private class BlackNumAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final BlackNumInfo info = list.get(position);
			ViewHolder viewHolder;
			View view;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_callsmssafe, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_itemcallsmssafe_blacknum = (TextView)view.findViewById(R.id.tv_itemcallsmssafe_blacknum);
				viewHolder.tv_itemcallsmssafe_mode = (TextView)view.findViewById(R.id.tv_itemcallsmssafe_mode);
				viewHolder.iv_itemcallsmssafe_delete = (ImageView)view.findViewById(R.id.iv_itemcallsmssafe_delete);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.tv_itemcallsmssafe_blacknum.setText(info.getBlacknum());
			int mode = info.getMode();
			switch (mode) {
			
			case BlackNumDao.CALL:
				viewHolder.tv_itemcallsmssafe_mode.setText("电话拦截");
				break;
			case BlackNumDao.SMS:
				viewHolder.tv_itemcallsmssafe_mode.setText("短信拦截");
				break;
			case BlackNumDao.ALL:
				viewHolder.tv_itemcallsmssafe_mode.setText("全部拦截");
				break;
			}
			
			viewHolder.iv_itemcallsmssafe_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
					builder.setMessage("您确认要删除黑名单号码:"+info.getBlacknum()+"?");
					builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mBlackNumDao.deleteBlackNum(info.getBlacknum());
							list.remove(position);
							blackNumAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					builder.create();
					builder.show();
				}
			});

			return view;
		}
		
		class ViewHolder{
			TextView tv_itemcallsmssafe_blacknum;
			TextView tv_itemcallsmssafe_mode;
			ImageView iv_itemcallsmssafe_delete;
		}
		
	}
}
