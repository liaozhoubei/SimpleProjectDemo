package com.example.myshop.bean;

import java.io.Serializable;


/**
 * Created by Bei on 2016/9/21.
 */

public class BaseBean implements Serializable {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
