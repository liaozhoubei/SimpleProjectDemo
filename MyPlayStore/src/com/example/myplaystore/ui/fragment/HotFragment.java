package com.example.myplaystore.ui.fragment;

import java.util.List;
import java.util.Random;

import com.example.myplaystore.http.protocol.HotProtocol;
import com.example.myplaystore.ui.view.FlowLayout;
import com.example.myplaystore.ui.view.MyFlowLayout;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.ui.view.fly.DrawableUtils;
import com.example.myplaystore.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 排行列表
 * @author ASUS-H61M
 *
 */
public class HotFragment extends BaseFragment {
	
	private List<String> data;

	@Override
	public View onCreateSuccessView() {
		
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		
//		FlowLayout flow = new FlowLayout(UIUtils.getContext());
		MyFlowLayout flow = new MyFlowLayout(UIUtils.getContext());
		int padding = UIUtils.dip2px(10);
//		flow.setPadding(padding, padding, padding, padding);
//		flow.setHorizontalSpacing(UIUtils.dip2px(4));
//		flow.setVerticalSpacing(UIUtils.dip2px(4));
		Random random = new Random();
		for (int i = 0; i <data.size(); i ++){
			TextView textView = new TextView(UIUtils.getContext());
			final String string = data.get(i);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			textView.setPadding(padding, padding, padding, padding);
			textView.setGravity(Gravity.CENTER);
			textView.setText(string);
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);
			
			int color = 0xffcecece;
			GradientDrawable bg_normal = DrawableUtils.getGradientDrawable(Color.rgb(r, g, b), UIUtils.dip2px(6));
			GradientDrawable bg_press = DrawableUtils.getGradientDrawable(color, UIUtils.dip2px(6));
			StateListDrawable selector = DrawableUtils.getSelector(bg_normal, bg_press);
			textView.setBackgroundDrawable(selector);
			
			flow.addView(textView);
			
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), string, Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		
		scrollView.addView(flow);
		return scrollView;
	}

	@Override
	public ResultState onLoad() {
		HotProtocol hotProtocol = new HotProtocol();
		data = hotProtocol.getData(0);
		
		return check(data);
	}
}
