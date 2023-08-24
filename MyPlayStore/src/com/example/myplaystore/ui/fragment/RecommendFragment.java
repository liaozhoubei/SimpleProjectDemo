package com.example.myplaystore.ui.fragment;

import java.util.List;
import java.util.Random;

import com.example.myplaystore.http.protocol.RecommendProtocol;
import com.example.myplaystore.ui.view.LoadingPager.ResultState;
import com.example.myplaystore.ui.view.fly.ShakeListener;
import com.example.myplaystore.ui.view.fly.ShakeListener.OnShakeListener;
import com.example.myplaystore.ui.view.fly.StellarMap;
import com.example.myplaystore.utils.UIUtils;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendFragment extends BaseFragment {
	private List<String> data;
	
	@Override
	public View onCreateSuccessView() {
		final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
		
		stellarMap.setAdapter(new RecommendAdapter());
		
		stellarMap.setRegularity(6, 9);
		int padding = UIUtils.dip2px(10);
		stellarMap.setInnerPadding(padding, padding, padding, padding);
		stellarMap.setGroup(0, true);
		
		ShakeListener shakeListener = new ShakeListener(UIUtils.getContext());
		shakeListener.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake() {
				// 摇晃调到下一页数据
				stellarMap.zoomIn();
			}
		});
		
		return stellarMap;
	}

	@Override
	public ResultState onLoad() {
		RecommendProtocol protocol = new RecommendProtocol();
		data = protocol.getData(0);
		return check(data);
	}
	
	class RecommendAdapter implements StellarMap.Adapter{

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public int getCount(int group) {
			int count = data.size() / getGroupCount();
			if (count == getGroupCount() - 1) {
				// 如果有余数，加载最后面
				count += data.size() % getGroupCount();
			}
			return count;
		}

		@Override
		public View getView(int group, int position, View convertView) {
			position += group * getCount(group - 1);
			final String keyword = data.get(position);
			
			TextView text = new TextView(UIUtils.getContext());
			text.setText(keyword);
			
			Random random = new Random();
			int size = 16 + random.nextInt(10);
			
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);

			text.setTextColor(Color.rgb(r, g, b));
			
			text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
				}
			});
			
			return text;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			if (isZoomIn){
				if (group > 0){
					group --;
				} else {
					group = getGroupCount() - 1;
				}
			} else {
				if (group < getGroupCount() - 1) {
					group ++ ;
				} else {
					group = 0;
				}
			}
			
			return group;
		}
		
	}
}
