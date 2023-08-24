package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	
	private String name;
	
	private Drawable icon;
	
	private long romSize;
	
	private String packageName;
	
	private boolean isUser;
	
	private boolean isChecked = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public long getRomSize() {
		return romSize;
	}

	public void setRomSize(long romSize) {
		this.romSize = romSize;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isUser() {
		return isUser;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "TaskInfo [name=" + name + ", icon=" + icon + ", romSize=" + romSize + ", packageName=" + packageName
				+ ", isUser=" + isUser + ", isChecked=" + isChecked + "]";
	}

	public TaskInfo(String name, Drawable icon, long romSize, String packageName, boolean isUser, boolean isChecked) {
		super();
		this.name = name;
		this.icon = icon;
		this.romSize = romSize;
		this.packageName = packageName;
		this.isUser = isUser;
		this.isChecked = isChecked;
	}

	public TaskInfo() {
		super();
	}

}
