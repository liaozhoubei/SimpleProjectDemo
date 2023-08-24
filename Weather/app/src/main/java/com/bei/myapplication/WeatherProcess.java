package com.bei.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Bei on 2016/3/10.
 */
public class WeatherProcess {
    private String mCity;
    private Context mContext;
    private Gson mGson;
    private WeatherRequest mWeatherRequest;
    private WeatherBean mWeatherBean;
    private WeatherResult mResults;
    private WeatherBean.HeWeather_data heWeatherData;
    private MainActivity mActivity;

    public WeatherProcess(Context context, String string, MainActivity activity) {
        mContext = context;
        mCity = string;
        mActivity = activity;

    }

    public void insertData() {
        mGson = new Gson();
        mWeatherRequest = new WeatherRequest(mCity);
        String jsonData = mWeatherRequest.getJsonString();
        if (jsonData == null) {
            sendMessageToMainActivity(mActivity.getString(R.string.error_network_connect));
        } else{
            mWeatherBean = mGson.fromJson(jsonData, WeatherBean.class);
            heWeatherData =  mWeatherBean.getHeWeather_data().get(0);
            mResults = new WeatherResult();
            mResults.status = heWeatherData.getStatus();
            if (mResults.status.equals("unknown city")) {
                Log.i("错误城市", mResults.status);
                sendMessageToMainActivity(mActivity.getString(R.string.get_erro_city));
            } else {
                mResults.city = heWeatherData.getBasic().getCity();
                mResults.cnty = heWeatherData.getBasic().getCnty();
                mResults.loc = heWeatherData.getBasic().getUpdate().getLoc();
                mResults.hum = heWeatherData.getNow().getHum();
                mResults.tmp = heWeatherData.getNow().getTmp();
                mResults.txt = heWeatherData.getNow().getCond().getTxt();
                mResults.dir = heWeatherData.getNow().getWind().getDir();
                mResults.sc = heWeatherData.getNow().getWind().getSc();

                WeatherDatabase dbHelper = new WeatherDatabase(mContext);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("city", mResults.city);
                values.put("cnty", mResults.cnty );
                values.put("loc", mResults.loc);
                values.put("hum", mResults.hum);
                values.put("tmp", mResults.tmp);
                values.put("txt", mResults.txt);
                values.put("dir",  mResults.dir);
                values.put("sc", mResults.sc);
                db.insert("weatherData", null, values);
                values.clear();
            }

        }

    }

    private void sendMessageToMainActivity(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.MESSAGE, str);
        Message msg = mActivity.getMessageHandler().obtainMessage();
        msg.what = MainActivity.NET_THREAD_MESSAGE;
        msg.setData(bundle);
        mActivity.getMessageHandler().sendMessage(msg);
    }
}
