package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.myshop.R;
import com.example.myshop.adapter.DividerItemDecoration;
import com.example.myshop.adapter.HWAdatper;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.example.myshop.utils.LogUtil;
import com.example.myshop.Contants;
import com.example.myshop.utils.Pager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import okhttp3.Response;

/**
 * Created by Bei on 2016/9/21.
 */
public class HotFragment extends BaseFragment implements Pager.OnPageListener{

    private String TAG = HotFragment.class.getSimpleName();
//    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();
//    private int currPage = 1;
//    private int pageSize = 10;
//    private int totalPage = 1;
//    private static final int STATE_NORMAL = 0;
//    private static final int STATE_REFREH = 1;
//    private static final int STATE_MORE = 2;
//
//    private int state = STATE_NORMAL;

    private HWAdatper mHotWaresAdapter;
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
        mHotWaresAdapter = new HWAdatper(getContext(),datas);

        mRecyclerView.setAdapter(mHotWaresAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mHotWaresAdapter.refreshData(datas);

        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mHotWaresAdapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mHotWaresAdapter.getDatas().size());
    }
}
