package com.example.myplaystore.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpRequest;

import com.example.myplaystore.http.HttpHelper;
import com.example.myplaystore.http.HttpHelper.HttpResult;
import com.example.myplaystore.utils.IOUtils;
import com.example.myplaystore.utils.StringUtils;
import com.example.myplaystore.utils.UIUtils;

public abstract class BaseProtocol<T> {

	public T getData(int index){
		String result = getCache(index);
		if (StringUtils.isEmpty(result)) {
			result = getDataFromServar(index);
		}
		
		if (result != null) {
			T parseData = parseData(result);
			return parseData;
		}
		return null;
		
	}

	private String getDataFromServar(int index) {
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey() + "?index=" + index + getParims());
		if (httpResult != null) {
			String string = httpResult.getString();
//			System.out.println(string);
			if (!StringUtils.isEmpty(string)) {
				setCache(index, string);
			}
			return string;
		}
		return null;
	}
	
	
	public abstract String getKey();
	public abstract String getParims();
	
	public void setCache(int index, String json){
		File cacheDir = UIUtils.getContext().getCacheDir(); // 本应用的缓存文件
		System.out.println("缓存目录" + cacheDir.toString());
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParims());
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(cacheFile);
			
			long deadline = System.currentTimeMillis() + 30 * 60* 1000; // 设置半小时有效期
			fileWriter.write(deadline + "\n");
			
			fileWriter.write(json);
			fileWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			IOUtils.close(fileWriter);
		}
	}
	
	public String getCache(int index){
		File cacheDir = UIUtils.getContext().getCacheDir(); // 本应用的缓存文件
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index + getParims());
		if (cacheFile.exists()) {
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(cacheFile));
				String deadline = bufferedReader.readLine();
				long deadTime = Long.parseLong(deadline);
				long nowTime = System.currentTimeMillis();
				if (nowTime < deadTime) {
					StringBuffer sb = new StringBuffer();
					String line;
					while((line = bufferedReader.readLine()) != null){
						sb.append(line);
					}
					return sb.toString();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				IOUtils.close(bufferedReader);
			}
		}
		
		return null;
	}
	
	public abstract T parseData(String result);
}
