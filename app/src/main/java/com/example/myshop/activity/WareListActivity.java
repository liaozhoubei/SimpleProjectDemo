package com.example.myshop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.adapter.HWAdatper;
import com.example.myshop.bean.Page;
import com.example.myshop.bean.Wares;
import com.example.myshop.utils.Pager;
import com.example.myshop.widget.MyToolbar;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Bei on 2016/9/27.
 */

public class WareListActivity extends Activity implements Pager.OnPageListener<Wares>,TabLayout.OnTabSelectedListener,View.OnClickListener{
    private static final String TAG = "WareListActivity";

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GIRD=2;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;
    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.toolbar)
    private MyToolbar mToolbar;

    private int orderBy = 0;
    private long campaignId = 0;

    private HWAdatper mWaresAdapter;

    private Pager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initToolBar();
        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID, 0);
        initTab();
        getData();
    }

    private void initToolBar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                WareListActivity.this.finish();
            }
        });
        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);

        mToolbar.setRightButtonOnClickListener(this);
    }

    private void getData() {
        pager= Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wares>>(){}.getType());

        pager.request();
    }

    private void initTab() {
        TabLayout.Tab tab = mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);

        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {

    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {

    }
}
