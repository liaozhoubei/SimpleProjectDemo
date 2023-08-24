package com.example.myshop.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myshop.Contants;
import com.example.myshop.R;
import com.example.myshop.bean.Wares;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.utils.ToastUtils;
import com.example.myshop.widget.MyToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;

/**
 * Created by Bei on 2016/9/28.
 */

public class WareDetailActivity extends Activity implements View.OnClickListener{

    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.toolbar)
    private MyToolbar mToolBar;

    private Wares mWare;

    private WebAppInterface mAppInterfce;

    private CartProvider cartProvider;

    private SpotsDialog mDialog;


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ViewUtils.inject(this);

        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if (serializable == null) {
            this.finish();
        }
        mWare = (Wares) serializable;
        cartProvider = new CartProvider(this);
        initToolBar();

        initWebView();
    }


    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置为tur时将不加载网络图片
        webSettings.setBlockNetworkImage(false);
        webSettings.setAppCacheEnabled(true);

        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        mAppInterfce = new WebAppInterface(this);
        // 其中appInterface要与html5页面中的方法一致，定义好的规范
        mWebView.addJavascriptInterface(mAppInterfce, "appInterface");
        mWebView.setWebViewClient(new WC());

    }

    private void initToolBar() {
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }

    class WC extends WebViewClient{
        // 页面加载完触发此方法
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mAppInterfce.showDetail();
        }
    }

    class WebAppInterface {

        private Context mContext;

        public WebAppInterface(Context context) {
            mContext = context;
        }

        // 调用JavaScript中的代码
        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail(" + mWare.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id){
            cartProvider.put(mWare);
            ToastUtils.show(mContext,"立刻购买");
        }

        @JavascriptInterface
        public void addToCart(long id) {
            cartProvider.put(mWare);
            ToastUtils.show(mContext, "已添加到购物车");
        }

    }
}
