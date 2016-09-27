package com.example.myshop.http;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Bei on 2016/9/22.
 * 对缓冲进度条的简单封装
 */

public abstract class SpotsCallBack<T> extends BaseCallback<T> {

    private SpotsDialog mSpotsDialog;

    public SpotsCallBack(Context context) {
        mSpotsDialog = new SpotsDialog(context);
    }

    public void showDialog() {
        mSpotsDialog.show();
    }

    public void dismissDialog() {
        if (mSpotsDialog != null) {
            mSpotsDialog.dismiss();
        }
    }

    public void setMessage(String message) {
        mSpotsDialog.setMessage(message);
    }

    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
    }


    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }


}
