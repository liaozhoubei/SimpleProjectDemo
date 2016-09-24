package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.myshop.R;
import com.example.myshop.bean.Wares;
import com.example.myshop.utils.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Bei on 2016/9/24.
 * 适用于HotFragment中RecyclerView的Adapter
 */

public class HWAdatper extends SimpleAdapter<Wares>{

    public HWAdatper(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
    }
}
