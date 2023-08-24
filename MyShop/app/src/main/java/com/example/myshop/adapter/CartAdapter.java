package com.example.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.bean.ShoppingCart;
import com.example.myshop.utils.CartProvider;
import com.example.myshop.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Bei on 2016/9/26.
 * 购物车适配器
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {
    public static final String TAG = "CartAdapter";

    private CheckBox checkBox;
    private TextView textView;

    private CartProvider cartProvider;


    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView tv) {
        super(context, R.layout.template_cart, datas);

        setCheckBox(checkBox);
        setTextView(tv);

        cartProvider = new CartProvider(context);

        setOnItemClickListener(this);

        showTotalPrice();


    }


    @Override
    protected void bindData(BaseViewHolder viewHoder, final ShoppingCart item) {

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        // 自定义的数量增减视图
        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHoder.getView(R.id.num_control);

        numberAddSubView.setValue(item.getCount());

        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            // 当增加数量的时候
            @Override
            public void onButtonAddClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();


            }

            // 当减少数量的时候
            @Override
            public void onButtonSubClick(View view, int value) {

                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });


    }


    /**
     * 获得购物车商品的总价
     * @return  返回商品总价格
     */
    private float getTotalPrice() {

        float sum = 0;
        if (!isNull())
            return sum;

        for (ShoppingCart cart :
                datas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }

        return sum;
    }


    /**
     * 展示购物车商品的价格
     */
    public void showTotalPrice() {

        float total = getTotalPrice();
        // 使用HTML将价格颜色改变
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }


    /**
     * 判断购物车是否
     * @return
     */
    private boolean isNull() {
        return (datas != null && datas.size() > 0);
    }


    @Override
    public void onItemClick(View view, int position) {

        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);

        checkListen();
        showTotalPrice();

    }

    /**
     * 监听购物车商品的选中事件
     */
    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();

            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }

            if (count == checkNum) {
                checkBox.setChecked(true);
            }

        }
    }

    /**
     *  选择购物车下发 全选 checkbox之后，所有购物车设置为全选或者全不选中
     * @param isChecked 商品是否被选中，true为选择，false为不选中
     */
    public void checkAll_None(boolean isChecked) {
        if (!isNull())
            return;
        int i = 0;
        for (ShoppingCart cart : datas) {
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }


    // 删除购物车中的信息
    public void delCart() {
        if (!isNull())
            return;

//      此方法会报错
//        for (ShoppingCart cart : datas){
//
//            if(cart.isChecked()){
//                int position = datas.indexOf(cart);
//                cartProvider.delete(cart);
//                datas.remove(cart);
//                notifyItemRemoved(position);
//            }
//        }

        for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {

            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                int position = datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }

        }

    }


    public void setTextView(TextView textview) {
        this.textView = textview;
    }

    // 如果
    public void setCheckBox(CheckBox ck) {
        this.checkBox = ck;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();

            }
        });
    }

}
