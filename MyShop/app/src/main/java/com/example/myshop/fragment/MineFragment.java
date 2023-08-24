package com.example.myshop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myshop.Contants;
import com.example.myshop.MyApplication;
import com.example.myshop.R;
import com.example.myshop.activity.AddressListActivity;
import com.example.myshop.activity.LoginActivity;
import com.example.myshop.activity.MyFavoriteActivity;
import com.example.myshop.activity.MyOrderActivity;
import com.example.myshop.bean.User;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Bei on 2016/9/21.
 */
public class MineFragment extends BaseFragment{


    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {
        showUser();
    }

    /**
     * 暂时登录用户界面
     */
    private void showUser() {
        User user = MyApplication.getInstance().getUser();
        if (user == null) {
            mbtnLogout.setVisibility(View.GONE);
            mTxtUserName.setText(R.string.to_login);
        } else {
            mbtnLogout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty((user.getLogo_url()))){
                Picasso.with(getActivity()).load(Uri.parse(user.getLogo_url())).into(mImageHead);

            }
            mTxtUserName.setText(user.getUsername());
        }
    }


    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLoginActivity(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }


        @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view){

//        startActivity(new Intent(getActivity(), MyOrderActivity.class),true);
    }


    @OnClick(R.id.txt_my_address)
    public void toAddressActivity(View view){

//        startActivity(new Intent(getActivity(), AddressListActivity.class),true);
    }

    @OnClick(R.id.txt_my_favorite)
    public void toFavoriteActivity(View view){

//        startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        showUser();
    }
}
