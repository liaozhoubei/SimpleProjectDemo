package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Bei on 2016/9/23.
 */

public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.HotWaresViewHolder>{

    private List<Wares> mDatas;
    private LayoutInflater mInflater;

    public HotWaresAdapter( List<Wares> mDatas){
        this.mDatas = mDatas;
    }

    @Override
    public HotWaresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.template_hot_wares, null);
        HotWaresViewHolder viewHolder = new HotWaresViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HotWaresViewHolder holder, int position) {
        Wares wares = mDatas.get(position);

        holder.draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.textTitle.setText(wares.getName());
        holder.textPrice.setText("￥"+wares.getPrice());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class HotWaresViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView draweeView;
        TextView textTitle;
        TextView textPrice;

        public HotWaresViewHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            textTitle= (TextView) itemView.findViewById(R.id.text_title);
            textPrice= (TextView) itemView.findViewById(R.id.text_price);
        }
    }
}
