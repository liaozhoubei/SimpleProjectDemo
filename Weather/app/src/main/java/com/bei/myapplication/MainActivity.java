package com.bei.myapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    public static final int NET_THREAD_MESSAGE = 1000;
    public static final String MESSAGE = "message";
    private MessageHandler mMessageHandler;
    private WeatherProcess mWeatherProcess;
    private EditText mEditText;
    private String mCity = null;
    private TextView mText;
    private MainActivity mActivity = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mText = (TextView) findViewById(R.id.text_test);
        mMessageHandler = new MessageHandler(this);
        wheather wheatherDefault = new wheather();
        wheatherDefault.execute();
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果有网络 则立即获取数据
                if (InternetUtil.isNetworkConnected(v.getContext())) {
                        mCity = mEditText.getText().toString();
                        wheather wh = new wheather();
                        wh.execute();
                    Toast.makeText(v.getContext(), R.string.getting_update, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), getString(R.string.error_network_connect), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


// 异步线程，查询并显示天气
    public class wheather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (mCity == null) {
                mWeatherProcess = new WeatherProcess(MainActivity.this, "beijing", mActivity);
            }else {
                mWeatherProcess = new WeatherProcess(MainActivity.this, mCity, mActivity);
            }
            mWeatherProcess.insertData();
            ContentResolver contentResolver = getContentResolver();
            Uri uri = Uri.parse(URIList.WEATHER_URI);
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            WeatherResult result = new WeatherResult();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    result.city = cursor.getString(cursor.getColumnIndex("city"));
                    result.cnty =cursor.getString(cursor.getColumnIndex("cnty"));
                    result.loc = cursor.getString(cursor.getColumnIndex("loc"));
                    result.hum = cursor.getString(cursor.getColumnIndex("hum"));
                    result.tmp = cursor.getString(cursor.getColumnIndex("tmp"));
                    result.txt = cursor.getString(cursor.getColumnIndex("txt"));
                    result.dir = cursor.getString(cursor.getColumnIndex("dir"));
                    result.sc = cursor.getString(cursor.getColumnIndex("sc"));
                }
                cursor.close();
            }
            String data = "城市：" + result.cnty + "," + result.city + "\n\n更新时间：" +  result.loc +  "\n\n湿度："
                    + result.hum +"\n\n气温：" + result.tmp + "°C\n天气状况：" + result.txt
                    + "\n风向：" + result.dir + "\n风力等级："  + result.sc;

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mText.setText(s);
        }
    }

    public MessageHandler getMessageHandler() {
        return mMessageHandler;
    }

    public static class MessageHandler extends Handler {
        public static WeakReference<MainActivity> sMainActivityWeakReference;

        public MessageHandler(MainActivity mainActivity) {
            sMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = sMainActivityWeakReference.get();
            switch (msg.what) {
                case NET_THREAD_MESSAGE:
                    Toast.makeText(activity, msg.getData().getString(MESSAGE), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

}
