package com.example.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.myshop.R;
import com.example.myshop.adapter.CardViewtemDecortion;
import com.example.myshop.adapter.DividerItemDecoration;
import com.example.myshop.adapter.HomeCatgoryAdapter;
import com.example.myshop.bean.Banner;
import com.example.myshop.bean.Campaign;
import com.example.myshop.bean.HomeCampaign;
import com.example.myshop.bean.HomeCategory;
import com.example.myshop.http.BaseCallback;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.http.SpotsCallBack;
import com.example.myshop.utils.LogUtil;
import com.example.myshop.widget.Contants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.R.string.ok;
import static java.security.AccessController.getContext;


/**
 * Create by Bei
 */
public class HomeFragment extends Fragment {
    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;
    private Gson mGson = new Gson();
    private List<Banner> mBanners;
    private OkHttpHelper mOkHttpHelper = com.example.myshop.http.OkHttpHelper.getIntance();

    private static final String TAG = "HomeFragment";
    private HomeCatgoryAdapter mHomeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        mIndicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        initRecyclerView(view);
        requestImage();
        return view;
    }

    private void requestImage(){
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
//        OkHttpClient httpClient = new OkHttpClient();
//        //FormEncodingBuilder在okhttp3中已经被弃用
////        FormEncodingBuilder builder = new FormEncodingBuilder().add("type", "1").build();
//        RequestBody requestBody = new FormBody.Builder().add("type", "1").build();
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        httpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String str = response.body().string();
//                    Type type = new TypeToken<List<Banner>>(){}.getType();
//                    mBanners = mGson.fromJson(str, type);
//                    initSlider();
//                }
//            }
//        });
        mOkHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerview);
//        List<HomeCategory> datas = new ArrayList<>(15);
//        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
//        datas.add(category);
//
//        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
//        datas.add(category);
//        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
//        datas.add(category);
//
//        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
//        datas.add(category);
//
//        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
//        datas.add(category);
//        mHomeAdapter = new HomeCatgoryAdapter(datas);
//        mRecyclerView.setHasFixedSize(true);
//        // 设置RecyclerView的分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecortion());
//        // 设置RecyclerView的布局方式，可以是LinearLayout/GraidLayout等
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(mHomeAdapter);
        mOkHttpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

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
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns){
        mHomeAdapter = new HomeCatgoryAdapter(homeCampaigns, getContext());
//        mRecyclerView.setHasFixedSize(true);

        mHomeAdapter.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCompaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(), campaign.getTitle() + "点击了什么东西", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置RecyclerView的分割线
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());
        // 设置RecyclerView的布局方式，可以是LinearLayout/GraidLayout等
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHomeAdapter);
    }

    // 设置手机轮播图
    private void initSlider() {

        if (mBanners != null) {
            for (Banner banner: mBanners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getDescription());
                // 设置图片视频屏幕
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);

            }
        }

        // 设置指示器
        mSliderLayout.setCustomIndicator(mIndicator);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSliderLayout != null) {
            mSliderLayout.stopAutoCycle();
        }

    }
}
