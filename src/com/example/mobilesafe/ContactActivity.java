package com.example.mobilesafe;

import java.util.HashMap;
import java.util.List;

import com.example.mobilesafe.engine.ContactEngine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * show contacts on the window. Send safePhoneNumber to SetUp3Activity
 * 
 * @author Bei
 *
 */
public class ContactActivity extends Activity {
	private List<HashMap<String, String>> list;
	private ListView lv_contact_contacts;
	private ProgressBar loading;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			lv_contact_contacts.setAdapter(new MyAdapter(getApplicationContext()));
			loading.setVisibility(View.INVISIBLE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);
		loading = (ProgressBar) findViewById(R.id.loading);
		loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				SystemClock.sleep(3 * 1000);
				list = ContactEngine.getAllContactInfo(getApplicationContext());
				mHandler.sendEmptyMessage(0);
			};
		}.start();

		lv_contact_contacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("phone", list.get(position).get("phone"));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;

		public MyAdapter(Context context) {
			layoutInflater = LayoutInflater.from(context);
		}

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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = (View) layoutInflater.inflate(R.layout.item_contact, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_itemcontact_name = (TextView) convertView.findViewById(R.id.tv_itemcontact_name);
				viewHolder.tv_itemcontact_phone = (TextView) convertView.findViewById(R.id.tv_itemcontact_phone);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tv_itemcontact_name.setText(list.get(position).get("name"));
			viewHolder.tv_itemcontact_phone.setText(list.get(position).get("phone"));

			return convertView;
		}

		class ViewHolder {
			TextView tv_itemcontact_name;
			TextView tv_itemcontact_phone;
		}
	}


}
