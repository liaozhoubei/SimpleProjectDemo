package com.example.myshop.adapter;

import android.content.Context;

import com.example.myshop.R;
import com.example.myshop.bean.Category;

import java.util.List;

/**
 * Created by Bei on 2016/9/24.
 */

public class CategoryAdapter extends SimpleAdapter<Category>{
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void bindData(BaseViewHolder viewHoder, Category item) {
        viewHoder.getTextView(R.id.textView).setText(item.getName());
    }
}
