package com.example.myplaystore.ui.view.fly;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {

	public static GradientDrawable getGradientDrawable(int color, int radius){
		GradientDrawable shap = new GradientDrawable();
		
		shap.setShape(GradientDrawable.RECTANGLE);
		shap.setCornerRadius(radius);
		shap.setColor(color);
		return shap;
	}
	
	
	public static StateListDrawable getSelector(Drawable normal, Drawable press){
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[]{android.R.attr.state_pressed}, press);
		selector.addState(new int[]{}, normal);
		return selector;
	}
}
