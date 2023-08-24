package com.example.myshop.http;

import android.content.Context;
import android.content.Intent;

import com.example.myshop.MyApplication;
import com.example.myshop.R;
import com.example.myshop.activity.LoginActivity;
import com.example.myshop.utils.ToastUtils;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Bei on 2016/9/28.
 */

public abstract class SimpleCallback<T> extends BaseCallback<T> {
    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyApplication.getInstance().clearUser();

    }

}
