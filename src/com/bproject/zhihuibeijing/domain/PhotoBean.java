package com.bproject.zhihuibeijing.domain;

import java.util.ArrayList;

public class PhotoBean {
	public PhotosData data;
	
	public class PhotosData {
		public String more;
		public ArrayList<PhotoNews> news;
	}
	
	public class PhotoNews {
		public String listimage;
		public String title;
		public int id;
		public String url;
	}
}
