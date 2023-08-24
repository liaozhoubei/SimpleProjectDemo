package com.example.myshop.bean;

import java.io.Serializable;

/**
 * Created by Bei on 2016/9/26.
 * 购物车的bean类
 */

public class ShoppingCart extends Wares implements Serializable {
    private int count;
    private boolean isChecked = true;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
