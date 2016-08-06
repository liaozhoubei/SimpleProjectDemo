package com.example.myplaystore.ui.fragment;

import java.util.HashMap;

public class FragmentFactory {

	private static HashMap<Integer, BaseFragment> hashMap = new HashMap<Integer, BaseFragment>();

	public static BaseFragment createFragment(int pos) {
		BaseFragment fragment = hashMap.get(pos);
		if (fragment == null) {
			switch (pos) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new AppFragment();
				break;
			case 2:
				fragment = new GameFragment();
				break;
			case 3:
				fragment = new SubjectFragment();
				break;
			case 4:
				fragment = new RecommendFragment();
				break;
			case 5:
				fragment = new CategoryFragment();
				break;
			case 6:
				fragment = new HotFragment();
				break;

			default:
				break;
			}
			hashMap.put(pos, fragment);
		}
		return fragment;
	}

}
