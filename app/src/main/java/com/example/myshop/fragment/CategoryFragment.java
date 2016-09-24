package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.daimajia.slider.library.SliderLayout;
import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.adapter.BaseAdapter;
import com.example.myshop.adapter.CategoryAdapter;
import com.example.myshop.adapter.DividerGridItemDecoration;
import com.example.myshop.adapter.DividerItemDecoration;
import com.example.myshop.adapter.WaresAdapter;
import com.example.myshop.bean.Banner;
import com.example.myshop.bean.Category;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.http.BaseCallback;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Create by Bei
 */
public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLaout;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdatper;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getIntance();

    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private long category_id = 0;


    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this, view);

        requestCategoryData();
        requestBannerData();


        initRefreshLayout();
        return view;
    }

    private void initRefreshLayout() {
    }

    // 请求轮播图数据
    private void requestBannerData() {
    }

    /**
     * 从网络中获取分类商品数据
     */
    private void requestCategoryData() {
        String url = Contants.API.BANNER + "?type=1";
        mHttpHelper.get(url, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 将商品数据展示在页面中
     *
     * @param categories 分类商品数据列表
     */
    private void showCategoryData(final List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                category_id = category.getId();
                currPage = 1;
                state = STATE_NORMAL;

                requestWares(category_id);
            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    // 请求网络中相应页面的数据
    private void requestWares(long categoryId) {
        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 展示页面商品数据
     *
     * @param wares
     */
    private void showWaresData(List<Wares> wares) {
        switch (state) {

            case STATE_NORMAL:
                // 选择安装GridLayout或者LinearLayout方式展示布局
                if (mWaresAdatper == null) {
                    mWaresAdatper = new WaresAdapter(getContext(), wares);

                    mRecyclerviewWares.setAdapter(mWaresAdatper);

                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                } else {
                    mWaresAdatper.clear();
                    mWaresAdatper.addData(wares);
                }
                break;

            case STATE_REFREH:
                // 下拉刷新页面的数据
                mWaresAdatper.clear();
                mWaresAdatper.addData(wares);

                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                // 下拉加载更多数据
                mWaresAdatper.addData(mWaresAdatper.getDatas().size(), wares);
                mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;


        }
    }


}



