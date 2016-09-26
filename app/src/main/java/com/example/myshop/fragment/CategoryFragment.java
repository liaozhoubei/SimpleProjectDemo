package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
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
import com.example.myshop.utils.LogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Create by Bei
 */
public class CategoryFragment extends Fragment {

    private final String TAG = CategoryFragment.class.getSimpleName();

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

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

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
        LogUtil.d(TAG, TAG+ "初始化了");
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this, view);

        requestCategoryData();
        requestBannerData();
        initRefreshLayout();
        return view;
    }

    private void initRefreshLayout() {
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }
        });
    }

    private void refreshData() {
        currPage =1;

        state=STATE_REFREH;
        requestWares(category_id);

    }

    private void loadMoreData(){

        currPage = ++currPage;
        state = STATE_MORE;
        requestWares(category_id);

    }

    /**
     * 从网络中获取分类商品数据
     */
    private void requestCategoryData() {
//        String url = Contants.API.BANNER + "?type=1";
        String url = Contants.API.BASE_URL +"category/list";;
        mHttpHelper.get(url, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                if(categories !=null && categories.size()>0)
                    category_id = categories.get(0).getId();
                requestWares(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 将左侧商品类目展示在页面中
     *
     * @param categories 分类商品数据列表
     */
    private void showCategoryData(final List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);
        // 点击类目时加载右侧商品信息
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.d(TAG, TAG + "点击刷新商品");
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

    // 请求轮播图数据
    private void requestBannerData() {
        String url = Contants.API.BANNER + "?type=1";
        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                ShowSliderView(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    // 设置轮播图
    private void ShowSliderView(List<Banner> banners) {

        if (banners != null) {
            for (Banner banner: banners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getDescription());
                // 设置图片视频屏幕
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);

            }
        }

        // 设置转场效果
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        // 切换时间
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                LogUtil.d(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
//                LogUtil.d(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                LogUtil.d(TAG,"onPageScrollStateChanged");
            }
        });
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
                LogUtil.d(TAG, TAG + "点击展示商品商品");
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
     * 展示页面右侧商品数据
     *
     * @param wares
     */
    private void showWaresData(List<Wares> wares) {
        switch (state) {

            case STATE_NORMAL:
                // 当mWaresAdatper为空的时候加载
                if (mWaresAdatper == null) {
                    mWaresAdatper = new WaresAdapter(getContext(), wares);

                    ShowRecyclerViewWaresData();
                } else {
                    // 当mWaresAdatper不为空的时候重新加载
                    // 若不重新价值会导致商品间隙变大，原因未知
                    mWaresAdatper.clear();
                    mWaresAdatper.addData(wares);

                    ShowRecyclerViewWaresData();
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

    // RecyclerView将右侧商品数据展示出来
    public void ShowRecyclerViewWaresData(){
        mRecyclerviewWares.setAdapter(mWaresAdatper);
        mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
        mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }


}



