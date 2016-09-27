package com.example.myshop.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.Tab;
import com.example.myshop.fragment.CartFragment;
import com.example.myshop.fragment.CategoryFragment;
import com.example.myshop.fragment.HomeFragment;
import com.example.myshop.fragment.HotFragment;
import com.example.myshop.fragment.MineFragment;

import com.example.myshop.widget.MyToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Bei
 */
public class MainActivity extends AppCompatActivity {
    private FragmentTabHost mTabhost;
    private LayoutInflater mInflater;
    private List<Tab> mTabs = new ArrayList<>(5);
    private MyToolbar myToolbar;
    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (MyToolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(myToolbar);
        initTab();
    }

    /**
     * 初始化底部标签栏
     */
    private void initTab() {
        Tab tab_home = new Tab(HomeFragment.class, R.string.home, R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotFragment.class, R.string.hot, R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class, R.string.catagory, R.drawable.selector_icon_category);
        Tab tab_cart = new Tab(CartFragment.class, R.string.cart, R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class, R.string.mine, R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);


        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpc = mTabhost.newTabSpec(getString(tab.getTitle()));
            tabSpc.setIndicator(buildIndicator(tab));
            mTabhost.addTab(tabSpc, tab.getFragment(), null);
        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId == getString(R.string.cart)) {
                    refData();
                } else {
                    myToolbar.showSearchView();
                    myToolbar.hideTitleView();
                    myToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });

        // 设置没有分隔线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);

    }

    /**
     * 标签切换到CartFragment的时候改变Toolbar样式
     */
    private void refData() {
        if (cartFragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
                cartFragment.changeToolbar();
            }
        } else {
            cartFragment.refData();
            cartFragment.changeToolbar();
        }
    }


    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }
}
