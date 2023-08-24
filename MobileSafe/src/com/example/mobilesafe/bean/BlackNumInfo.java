package com.example.mobilesafe.bean;
/**
 * 存储黑名单的bean对象
 * @author ASUS-H61M
 *
 */
public class BlackNumInfo {
	private String blacknum;
	private int mode;
	public String getBlacknum() {
		return blacknum;
	}
	
	
	public BlackNumInfo(String blacknum, int mode) {
		super();
		this.blacknum = blacknum;
		if (mode >=0 && mode <= 2) {
			this.mode = mode;
		}else{
			this.mode = 0;
		}
	}


	public void setBlacknum(String blacknum) {
		this.blacknum = blacknum;
	}
	public int getMode() {
		if (mode >=0 && mode <= 2) {
			this.mode = mode;
		}else{
			this.mode = 0;
		}
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlackNumInfo [blacknum=" + blacknum + ", mode=" + mode + "]";
	}
	

}
