package com.bei.myapplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bei on 2016/3/10.
 */
public class WeatherRequest {
    private String httpArg;
    // 在http://apistore.baidu.com/apiworks/servicedetail/478.html获取自己的百度API key
    private static String API_KEY = "百度API KEY，请自己获取";;
    String httpUrl = "http://apis.baidu.com/heweather/weather/free";

    public WeatherRequest(String string) {
        httpArg = "city=" + string;
    }

    public String getJsonString() {
        return request(httpUrl, httpArg);
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  API_KEY);
            connection.connect();

            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
