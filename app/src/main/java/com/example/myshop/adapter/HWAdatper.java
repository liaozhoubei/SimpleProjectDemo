package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.example.myshop.R;
import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.bean.Wares;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.utils.LogUtil;
import com.example.myshop.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Bei on 2016/9/24.
 * 适用于HotFragment中RecyclerView的Adapter
 */

public class HWAdatper extends SimpleAdapter<Wares> {

    private CartProvider cartProvider;

    public HWAdatper(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
        cartProvider = new CartProvider(context);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());

        Button button = viewHolder.getButton(R.id.btn_add);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cartProvider.put(wares);

                    ToastUtils.show(context, "已添加到购物车");
                }
            });
        }
    }

    public ShoppingCart convertData(Wares item) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());
        return cart;
    }

    /**
     * 重新设置页面布局
     * @param layoutId 布局视图id
     */
    public void  resetLayout(int layoutId){
        this.layoutResId  = layoutId;
        notifyItemRangeChanged(0,getDatas().size());
    }
}
