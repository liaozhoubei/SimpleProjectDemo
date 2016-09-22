package com.example.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.Campaign;
import com.example.myshop.bean.Category;
import com.example.myshop.bean.HomeCampaign;
import com.example.myshop.bean.HomeCategory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Bei on 2016/9/21.
 */

public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.HomeViewHolder>{



    private List<HomeCampaign> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnCompaignClickListener onCampaignClickListener;


    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;
    private HomeCampaign homeCampaign;

    public HomeCatgoryAdapter(List<HomeCampaign> list, Context context) {
        mList = list;
        this.mContext = context;
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
        homeCampaign = mList.get(position);
        holder.textTitle.setText(homeCampaign.getTitle());


        // 使用Picasso获取网络中的图片
        RequestCreator CpOneImageUrl = Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl());
        // 将网络图片加载到imageViewBig中
        CpOneImageUrl.into(holder.imageViewBig);

        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);

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

    public void setOnCampaignClickListener(OnCompaignClickListener onCampaignClickListener){
        this.onCampaignClickListener = onCampaignClickListener;
    }

    class holl extends RecyclerView.ViewHolder{

        public holl(View itemView) {
            super(itemView);
        }
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
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
            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (onCampaignClickListener != null){
                switch (view.getId()){
                    case R.id.imgview_big:
                        onCampaignClickListener.onClick(view, homeCampaign.getCpOne());
                        break;
                    case R.id.imgview_small_top:
                        onCampaignClickListener.onClick(view, homeCampaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom:
                        onCampaignClickListener.onClick(view, homeCampaign.getCpThree());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public interface OnCompaignClickListener{
        void onClick(View view, Campaign campaign) ;
    }
}




