package com.bei.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Bei on 2016/3/10.
 */
public class WeatherProvider extends ContentProvider{
    private static final int QUERY_SUCCESS_CODE = 1;
    private static final int INSERT_SUCCESS_CODE = 2;
    private static final int DEL_SUCCESS_CODE = 3;
    private static final int UPDATE_SUCCESS_CODE = 4;
    private static UriMatcher sUriMatcher;
    public static final int URI_MATCH_WEATHER = 1;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(URIList.AUTHORITY, WeatherDatabase.TABLE_NAME_WEATHER, URI_MATCH_WEATHER);
        sUriMatcher.addURI(URIList.AUTHORITY, "query", QUERY_SUCCESS_CODE);
        sUriMatcher.addURI(URIList.AUTHORITY, "insert", INSERT_SUCCESS_CODE);
        sUriMatcher.addURI(URIList.AUTHORITY, "del", DEL_SUCCESS_CODE);
        sUriMatcher.addURI(URIList.AUTHORITY, "update", UPDATE_SUCCESS_CODE);
    }

    private  WeatherDatabase mWeatherOpenHelper = new WeatherDatabase(getContext());

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = sUriMatcher.match(uri);
        if (code == QUERY_SUCCESS_CODE) {
            WeatherDatabase helper = new WeatherDatabase(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            return db.query("weatherData", projection, selection, selectionArgs, null, null, sortOrder);
        }
        throw new IllegalArgumentException("URI not found");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = sUriMatcher.match(uri);
        if (code == INSERT_SUCCESS_CODE) {
            WeatherDatabase helper = new WeatherDatabase(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            long id = db.insert("weatherData", null, values);
            return ContentUris.withAppendedId(uri, id);
        }
        throw new IllegalArgumentException("URI not found");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = sUriMatcher.match(uri);
        if (code == DEL_SUCCESS_CODE) {
            WeatherDatabase helper = new WeatherDatabase(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            return db.delete("weatherData", selection, selectionArgs);
        }
        throw new IllegalArgumentException("URI not found");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int code = sUriMatcher.match(uri);
        if (code == UPDATE_SUCCESS_CODE) {
            WeatherDatabase helper = new WeatherDatabase(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            return db.update("weatherData", values, selection, selectionArgs);
        }
        throw new IllegalArgumentException("URI not found");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

}
