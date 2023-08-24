package com.example.myshop.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.example.myshop.MyApplication;
import com.example.myshop.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Bei on 2016/9/22.
 * 简单封装OkHttp3的工具类
 */

public class OkHttpHelper {
    public static final int TOKEN_MISSING = 401;// token 丢失
    public static final int TOKEN_ERROR = 402; // token 错误
    public static final int TOKEN_EXPIRE = 403; // token 过期

    public static final String TAG = "OkHttpHelper";

    private static OkHttpHelper mInstance;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;

    private Handler mHandler;

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        mGson = new Gson();
        // 从looper中获取handler
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }


    public void get(String url, Map<String, Object> param, BaseCallback callback) {

        Request request = buildGetRequest(url, param);

        doRequest(request, callback);

    }

    /**
     * 直接获得URL链接
     * @param url 可直接访问的URL地址
     * @param callback
     */
    public void get(String url, BaseCallback callback) {

        get(url, null, callback);
    }

    public void post(String url, Map<String, Object> params, BaseCallback callback) {
        Request request = buildPostRequest(url, params);
        doRequest(request, callback);
    }


    public void doRequest(final Request request, final BaseCallback callback) {

        callback.onBeforeRequest(request);

        mOkHttpClient.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response);
                callbackResponse(callback, response);

                if (response.isSuccessful()) {

                    String resultStr = response.body().string();

                    LogUtil.d(TAG, "result=" + resultStr);

                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {

                            Object obj = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, obj);
                        } catch (com.google.gson.JsonParseException e) { // Json解析的错误
                            callback.onError(response, response.code(), e);
                        }
                    }
                } else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE || response.code() == TOKEN_MISSING) {

                    callbackTokenError(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }
        });
    }


    private void callbackTokenError(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }


    private void callbackResponse(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    private Request buildPostRequest(String url, Map<String, Object> params) {
        return buildRequest(url, HttpMethodType.POST, params);
    }

    private Request buildGetRequest(String url, Map<String, Object> param) {
        return buildRequest(url, HttpMethodType.GET, param);
    }


    // 构建RequestBody的方法，如果是Get请求发出Get请求，Post亦然
    private Request buildRequest(String url, HttpMethodType methodType, Map<String, Object> params) {
        Request.Builder builder = new Request.Builder().url(url);
        if (methodType == HttpMethodType.POST) {
            RequestBody body = builderBody(params);
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, params);
            builder.url(url);
            builder.get();
        }
        return builder.build();
    }

    /**
     * 拼接URL字符串
     *
     * @param url
     * @param params
     * @return
     */
    private String buildUrlParams(String url, Map<String, Object> params) {

        if (params == null)
            params = new HashMap<>(1);

        String token = MyApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token))
            params.put("token", token);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }

        return url;
    }

    // 构建RequestBody的方法
    private RequestBody builderBody(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
//        if (params != null) {
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                builder.add(entry.getKey(), entry.getValue());
//            }
//        }
        if (params != null) {

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }
            // 登陆时的Tooken
            String token = MyApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token))
                builder.add("token", token);
        }
        return builder.build();
    }

    enum HttpMethodType {
        GET,
        POST,
    }

}
