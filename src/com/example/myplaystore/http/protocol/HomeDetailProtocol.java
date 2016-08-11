package com.example.myplaystore.http.protocol;

import com.example.myplaystore.domain.AppInfo;

public class HomeDetailProtocol extends BaseProtocol<AppInfo> {
	private String packageName;
	
	
	
	public HomeDetailProtocol(String packageName) {
		super();
		this.packageName = packageName;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return "detail";
	}

	@Override
	public String getParims() {
		// TODO Auto-generated method stub
		return "&packageName=" + packageName;
	}

	@Override
	public AppInfo parseData(String result) {
		// TODO Auto-generated method stub
		return null;
	}

}
