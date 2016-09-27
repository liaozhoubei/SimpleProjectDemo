package com.example.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myshop.activity.CreateOrderActivity;
import com.example.myshop.R;
import com.example.myshop.adapter.CartAdapter;
import com.example.myshop.adapter.DividerItemDecoration;
import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.http.OkHttpHelper;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.utils.LogUtil;
import com.example.myshop.widget.MyToolbar;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;


/**
 * Create by Bei
 */
public class CartFragment extends BaseFragment implements View.OnClickListener{

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;
    private static final String TAG = "CartFragment";


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    @ViewInject(R.id.toolbar)
    protected MyToolbar mToolbar;


    private CartAdapter mAdapter;
    private CartProvider cartProvider;


    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(TAG, TAG+ "初始化了");
        return  inflater.inflate(R.layout.fragment_cart,container,false);
    }

    @Override
    public void init() {

        cartProvider = new CartProvider(getActivity());

        changeToolbar();
        showData();
    }

    /**
     * 删除购物车中的商品
     * @param view
     */
    @OnClick(R.id.btn_del)
    public void delCart(View view){

        mAdapter.delCart();
    }

    /**
     * 下单购买按键
     * @param view
     */
    @OnClick(R.id.btn_order)
    public void toOrder(View view){

        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);

//        startActivity(intent,true);
    }


    /**
     * 在购物车中展示商品
     */
    private void showData(){
        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getActivity(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));


    }



    public void refData(){
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }


    /**
     * 改变Toolbar的样式，隐藏搜索栏
     */
    public void changeToolbar(){

        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    /**
     * 点击右上角编辑完成之后，更改购物车为正常样式
     */
    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }

    /**
     * 隐藏购物车删除控制的样式
     */
    private void  hideDelControl(){

        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }


    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if(ACTION_EDIT == action){
            showDelControl();
        }
        else if(ACTION_CAMPLATE == action){
            hideDelControl();
        }
    }
}
