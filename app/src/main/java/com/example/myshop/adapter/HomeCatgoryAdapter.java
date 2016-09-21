package com.example.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.Category;
import com.example.myshop.bean.HomeCategory;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Bei on 2016/9/21.
 */

public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeViewHolder>{



    private List<HomeCategory> mList;
    private LayoutInflater mInflater;

    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;

    public HomeCatgoryAdapter(List<HomeCategory> list) {
        mList = list;
    }


    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_R) {
            view = mInflater.inflate(R.layout.template_home_cardview2, parent, false);
            HomeViewHolder homeViewHolder = new HomeViewHolder(view);
            return homeViewHolder;
        } else {
            view = mInflater.inflate(R.layout.template_home_cardview, parent, false);
            HomeViewHolder homeViewHolder = new HomeViewHolder(view);
            return homeViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        HomeCategory category = mList.get(position);
        holder.textTitle.setText(category.getName());
        holder.imageViewBig.setImageResource(category.getImgBig());
        holder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
        holder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        } else {
            return VIEW_TYPE_L;
        }

    }
}

class HomeViewHolder extends RecyclerView.ViewHolder{
    TextView textTitle;
    ImageView imageViewBig;
    ImageView imageViewSmallTop;
    ImageView imageViewSmallBottom;

    public HomeViewHolder(View itemView) {
        super(itemView);
        textTitle = (TextView) itemView.findViewById(R.id.text_title);
        imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
        imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
        imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

    }
}
