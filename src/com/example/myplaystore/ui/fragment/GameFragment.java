package com.example.myplaystore.ui.fragment;

import com.example.myplaystore.ui.view.LoadingPager.ResultState;

import android.view.View;

public class GameFragment extends BaseFragment {
	@Override
	public View onCreateSuccessView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultState onLoad() {
		return ResultState.STATE_EMPTY;
	}
}
