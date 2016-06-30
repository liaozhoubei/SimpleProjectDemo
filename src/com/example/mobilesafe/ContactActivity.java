package com.example.mobilesafe;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ContactActivity extends Activity{
	private ListView lv_contact_contacts;
	private ProgressBar loading;
	private List<HashMap<String, String>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
//		loading = findViewById(R.id.loading);
//		lv_contact_contacts = findViewById(R.id.lv_contact_contacts);
//		myAdapter = new MyAdapter();
//		lv_contact_contacts.setAdapter(myAdapter);
		
	}

	

}
