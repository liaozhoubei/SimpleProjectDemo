package com.example.myshop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.bean.Wares;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Bei on 2016/9/26.
 *  购物车的工具类，用于购物车的增删查改
 */

public class CartProvider {
    public static final String CART_JSON = "cart_json";

    private SparseArray<ShoppingCart> datas = null;

    private Context mContext;

    public CartProvider(Context context) {
        mContext = context;
        datas = new SparseArray<>(10);
        listToSparse();
    }

    /**
     * 获得商品的ID号，判断购物车中是否存在此商品，如果存在则在购物车中的数目+1，不存在则在购物车中添加此商品
     * @param cart
     */
    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(cart.getId().intValue());
        if (temp != null ) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(), temp);
        commit();
    }

    // 添加货物到购物车
    public void  put(Wares wares) {
        ShoppingCart cart = convertData(wares);
        put(cart);

    }

    public void update(ShoppingCart cart){

        datas.put(cart.getId().intValue(),cart);
        commit();
    }

    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll(){

        return  getDataFromLocal();
    }

    private void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    private List<ShoppingCart> sparseToList(){
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i ++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }

    private void listToSparse(){
        List<ShoppingCart> carts =  getDataFromLocal();

        if(carts!=null && carts.size()>0){

            for (ShoppingCart cart:
                    carts) {

                datas.put(cart.getId().intValue(),cart);
            }
        }
    }

    public  List<ShoppingCart> getDataFromLocal(){
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return carts;
    }

    private ShoppingCart convertData(Wares wares) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(wares.getId());
        cart.setDescription(wares.getDescription());
        cart.setImgUrl(wares.getImgUrl());
        cart.setName(wares.getName());
        cart.setPrice(wares.getPrice());
        return cart;
    }




}
