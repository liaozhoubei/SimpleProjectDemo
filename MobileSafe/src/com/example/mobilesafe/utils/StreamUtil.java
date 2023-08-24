package com.example.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class StreamUtil  {
	/**
	 * 
	 * @param is InputStream from URL
	 * @return  get JSON String
	 * @throws IOException 
	 */
	public static String parseInpustreamUtil(InputStream is) throws IOException{
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		byte[] bt = new byte[1024];
//		int len = 0;
//		
//			while((len = is.read(bt)) != -1) {
//				bos.write(bt, 0, len);
//			}
//			String json = new String(bos.toString());
//			System.out.println(json);
//			bos.close();
//			return json;
		
		
		//字符流,读取流
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		//写入流
		StringWriter sw  = new StringWriter();
		//读写操作
		//数据缓冲区
		String str = null;
		while((str = br.readLine()) !=null){
			//写入操作
			sw.write(str);
		}
		//关流
		sw.close();
		br.close();
		return sw.toString();
	}
}
