package com.example.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.example.myshop.R;
import com.example.myshop.activity.WareDetailActivity;
import com.example.myshop.adapter.BaseAdapter;
import com.example.myshop.adapter.HWAdatper;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.utils.LogUtil;
import com.example.myshop.Contants;
import com.example.myshop.utils.Pager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Bei on 2016/9/21.
 */
public class HotFragment extends BaseFragment implements Pager.OnPageListener{

    private String TAG = HotFragment.class.getSimpleName();


    private HWAdatper mAdatper;
    private List<Wares> datas;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLaout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(TAG, TAG+ "初始化了");
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        return view;
    }

    @Override
    public void init() {
        Pager pager = Pager.newBuilder().setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true).setOnPageListener(this).setPageSize(20)
                .setRefreshLayout(mRefreshLaout).build(getContext(), new TypeToken<Page<Wares>>(){}.getType());
        pager.request();
    }

    @Override
    public void load(List datas, int totalPage, int totalCount) {
        mAdatper = new HWAdatper(getContext(),datas);
        // 设置点击事件，打开商品详情页
        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Wares wares = mAdatper.getItem(position);
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.WARE, wares);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mAdatper.refreshData(datas);

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mAdatper.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
    }
}
