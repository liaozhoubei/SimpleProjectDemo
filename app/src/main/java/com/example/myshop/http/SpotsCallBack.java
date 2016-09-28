package com.example.myshop.http;

import android.content.Context;

import com.example.myshop.adapter.SimpleAdapter;
import com.example.myshop.utils.ToastUtils;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Bei on 2016/9/22.
 * 对缓冲进度条的简单封装
 */

public abstract class SpotsCallBack<T> extends SimpleCallback<T> {

    private SpotsDialog mDialog;

    public SpotsCallBack(Context context) {
        super(context);

        initSpotsDialog();
    }


    private void initSpotsDialog() {

        mDialog = new SpotsDialog(mContext, "拼命加载中...");
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }


    public void setLoadMessage(int resId) {
        mDialog.setMessage(mContext.getString(resId));
    }


    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }


}
