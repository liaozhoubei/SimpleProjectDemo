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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import okhttp3.Response;

/**
 * Created by Bei on 2016/9/21.
 */
public class HotFragment extends Fragment {

    private String TAG = HotFragment.class.getSimpleName();
    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();
    private int currPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private HWAdatper mHotWaresAdapter;
    private List<Wares> datas;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLaout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ViewUtils.inject(this, view);
        initRefreshLayout();
        getData();
        return view;
    }

    private void initRefreshLayout() {
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currPage <= totalPage) {
                    loadMoreData();
                } else {
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {

        currPage = 1;

        state = STATE_REFREH;
        getData();

    }

    private void loadMoreData() {

        currPage = ++currPage;
        state = STATE_MORE;

        getData();

    }

    private void getData() {

        String url = Contants.API.WARES_HOT + "?curPage=" + currPage + "&pageSize=" + pageSize;
        mOkHttpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {


                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                LogUtil.d("TAG", "datas的数据不为空");
                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void showData() {

        switch (state) {
            case STATE_NORMAL:
                mHotWaresAdapter = new HWAdatper(getContext(), datas);
                mRecyclerView.setAdapter(mHotWaresAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

                break;

            case STATE_REFREH:
                mHotWaresAdapter.clear();
                mHotWaresAdapter.addData(datas);

                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;
            case STATE_MORE:
                mHotWaresAdapter.addData(mHotWaresAdapter.getDatas().size(),datas);
                mRecyclerView.scrollToPosition(mHotWaresAdapter.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

    }
}
