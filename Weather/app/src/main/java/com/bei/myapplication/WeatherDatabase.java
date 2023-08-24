package com.bei.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bei on 2016/3/10.
 */
public class WeatherDatabase extends SQLiteOpenHelper{
    public static final String WEATHER = "Weather";
    public static final int VERSION = 1;
    public static final String TABLE_NAME_WEATHER = "weatherData";

    public WeatherDatabase(Context context) {
        super(context, WEATHER, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_WEATHER + " (cnty varchar(20) not null, " +
                "city varchar(20) not null, " +
                "loc varchar(20) not null, " +
                "hum varchar(20) not null, " +
                "tmp varchar(20) not null, " +
                "txt varchar(20) not null, " +
                "dir varchar(20) not null, " +
                "sc varchar(20) not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
