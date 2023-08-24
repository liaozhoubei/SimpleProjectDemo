package com.example.myshop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Bei on 2016/9/24.
 * 对BaseAdapter的二次封装
 */

public abstract class SimpleAdapter<T> extends  BaseAdapter<T, BaseViewHolder>{


    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int LayoutResId, List<T> datas) {
        super(context, LayoutResId, datas);
    }
}
