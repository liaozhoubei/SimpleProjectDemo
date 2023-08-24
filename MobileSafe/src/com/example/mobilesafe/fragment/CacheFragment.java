package com.example.mobilesafe.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CacheFragment extends Fragment {
	private MyAdapter adapter;
	private TextView tv_cachefragment_text;
	private ProgressBar pb_cachefragment_progressbar;
	private ListView lv_cachefragment_caches;
	private List<CacheInfo> list;
	private PackageManager packageManager;
	private Button btn_cachefragment_clear;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		list = new ArrayList<CacheFragment.CacheInfo>();
		list.clear();
		View view = inflater.inflate(R.layout.fragment_cache, container, false);
		tv_cachefragment_text = (TextView) view.findViewById(R.id.tv_cachefragment_text);
		pb_cachefragment_progressbar = (ProgressBar) view.findViewById(R.id.pb_cachefragment_progressbar);
		lv_cachefragment_caches = (ListView) view.findViewById(R.id.lv_cachefragment_caches);
		btn_cachefragment_clear = (Button) view.findViewById(R.id.btn_cachefragment_clear);
		listViewItemClick();
		return view;
	}

	private void listViewItemClick() {
		lv_cachefragment_caches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				intent.setData(Uri.parse("package:" + list.get(position).getPackageName()));
				startActivity(intent);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		scannerCache();
	}

	private void scannerCache() {
		packageManager = getActivity().getPackageManager();
		new Thread() {
			public void run() {
				List<PackageInfo> installedApplications = packageManager.getInstalledPackages(0);
				pb_cachefragment_progressbar.setMax(installedApplications.size());
				int progress = 0;
				for (PackageInfo packageInfo : installedApplications) {
					SystemClock.sleep(100);
					final String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
					if (getActivity() != null) {

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tv_cachefragment_text.setText(name);
							}
						});
					}
					
					try {
						Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
						Method declaredMethod = loadClass.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
						Object invoke = declaredMethod.invoke(packageManager, packageInfo.packageName, mStatsObserver);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 

					progress++;
					pb_cachefragment_progressbar.setProgress(progress);

				}
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {

						

						@Override
						public void run() {
							adapter = new MyAdapter();
							tv_cachefragment_text.setVisibility(View.GONE);
							pb_cachefragment_progressbar.setVisibility(View.GONE);
							lv_cachefragment_caches.setAdapter(adapter);
							if (list.size() > 0) {
								btn_cachefragment_clear.setVisibility(View.VISIBLE);
								btn_cachefragment_clear.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										try {
											Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
											Method declaredMethod = loadClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
											declaredMethod.invoke(packageManager, Long.MAX_VALUE, new MyIPackageDataObserver());
										} catch (ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (NoSuchMethodException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IllegalAccessException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IllegalArgumentException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										list.clear();
										adapter.notifyDataSetChanged();
										btn_cachefragment_clear.setVisibility(View.GONE);
									}
								});
								
							}
						}
					});

				}

			};
		}.start();
	}
	
	
	IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) {
        	long cachesize = stats.cacheSize;
        	long codesize = stats.codeSize;
        	long datasize = stats.dataSize;
        	String cache = Formatter.formatFileSize(getActivity(), cachesize);
//        	String code = Formatter.formatFileSize(getActivity(), codesize);
//        	String data = Formatter.formatFileSize(getActivity(), datasize);
        	if (cachesize > 0){
        		list.add(new CacheInfo(stats.packageName, cache));
        	}
        	System.out.println("   cache:" + cache );
        }
	};
	
	
	private class MyIPackageDataObserver extends IPackageDataObserver.Stub{
		//当缓存清理完成之后调用
		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			
		}
	}
	

	class CacheInfo{
		private String packageName;
		private String cacheSize;
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String getCacheSize() {
			return cacheSize;
		}
		public void setCacheSize(String cacheSize) {
			this.cacheSize = cacheSize;
		}
		@Override
		public String toString() {
			return "CacheInfo [packageName=" + packageName + ", cacheSize=" + cacheSize + "]";
		}
		public CacheInfo(String packageName, String cacheSize) {
			super();
			this.packageName = packageName;
			this.cacheSize = cacheSize;
		}
		public CacheInfo() {
			super();
		}
		
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder viewHolder;
			if (convertView == null) {
				view = View.inflate(getActivity(), R.layout.item_cache, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_itemcache_icon = (ImageView) view.findViewById(R.id.iv_itemcache_icon);
				viewHolder.tv_itemcache_name = (TextView) view.findViewById(R.id.tv_itemcache_name);
				viewHolder.tv_itemcache_size = (TextView) view.findViewById(R.id.tv_itemcache_size);
				view.setTag(viewHolder);
			} else {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}
			CacheInfo cacheInfo = list.get(position);
			
			String cacheSize = cacheInfo.getCacheSize();
			
			try {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(cacheInfo.getPackageName(), 0);
				Drawable loadIcon = applicationInfo.loadIcon(packageManager);
				String name = applicationInfo.loadLabel(packageManager).toString();
				
				viewHolder.iv_itemcache_icon.setImageDrawable(loadIcon);
				viewHolder.tv_itemcache_name.setText(name);
				viewHolder.tv_itemcache_size.setText(cacheSize);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return view;
		}

		
	}
	static class ViewHolder {
		ImageView iv_itemcache_icon;
		TextView tv_itemcache_name, tv_itemcache_size;
		
	}
}
