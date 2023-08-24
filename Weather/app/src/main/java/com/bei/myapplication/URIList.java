package com.bei.myapplication;

/**
 * Created by Bei on 2016/3/10.
 */
public class URIList {
    public static final String CONTENT = "content://";
    public static final String AUTHORITY = "com.bei.myapplication";

    public static final String WEATHER_URI = CONTENT + AUTHORITY + "/" + WeatherDatabase.TABLE_NAME_WEATHER;
}
