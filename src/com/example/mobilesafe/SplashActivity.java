package com.example.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final int MSG_UPDATE_DIALOG = 1;

	protected static final int MSG_ENTER_HOME = 2;

	protected static final int MSG_SERVER_ERROR = 3;

	protected static final int MSG_URL_ERRO = 4;

	protected static final int MSG_IO_ERROR = 5;

	protected static final int MSG_JSON_ERRO = 6;

	private String code;
	private String des;
	private String apkurl;

	private TextView tv_splash_version;
	private TextView tv_spalsh_plan;
	// get the update config from SharedPreferences
	private SharedPreferences sp;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case MSG_ENTER_HOME:
				enterMainActivity();
				break;
			case MSG_SERVER_ERROR:
				enterMainActivity();
				break;
			case MSG_URL_ERRO:
				Toast.makeText(getApplicationContext(), "错误代码：" + MSG_URL_ERRO, Toast.LENGTH_SHORT).show();
				enterMainActivity();
				break;
			case MSG_IO_ERROR:
				Toast.makeText(getApplicationContext(), "错误代码：" + MSG_IO_ERROR, Toast.LENGTH_SHORT).show();
				enterMainActivity();
				break;
			case MSG_JSON_ERRO:
				Toast.makeText(getApplicationContext(), "错误代码：" + MSG_JSON_ERRO, Toast.LENGTH_SHORT).show();
				enterMainActivity();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_spalsh_plan = (TextView) findViewById(R.id.tv_spalsh_plan);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("Version：" + getVersion());
		
		// if update config from sharedPreferences is true, show updata dialog
		sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("update", true)){
			update();
		} else {
			new Thread(){
				@Override
				public void run() {
					SystemClock.sleep(2 * 1000);
					enterMainActivity();
					super.run();
				}
			}.start();;
		}
		
		copyDB();// copy database to app

//		Intent intent = new Intent(this, AddressService.class);
//		startService(intent);
	}
	
	// Copy DataBase from asset
	private void copyDB() {
		new Thread(){
			@Override
			public void run() {
				super.run();
				File file = new File(getFilesDir(), "address.db");
				if (!file.exists()) {
					AssetManager assetManager = getAssets();
					InputStream is = null;
					FileOutputStream fos = null;
					try{
						is = assetManager.open("address.db");
						fos = new FileOutputStream(file);
						byte[] bt = new byte[1024];
						int len = -1;
						while((len = is.read(bt)) != -1) {
							fos.write(bt, 0, len);
						}
					} catch(IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is.close();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		}.start();

		
	}
	/**
	 * show the update dialog in the window
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder updateDialog = new AlertDialog.Builder(SplashActivity.this);
		updateDialog.setTitle("新版本:" + code);
		updateDialog.setMessage(des);
		updateDialog.setIcon(R.drawable.ic_launcher);
		updateDialog.setCancelable(false);
		updateDialog.setPositiveButton("yes", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("我要更新版本");
				downNewVersion();
			}

		});
		updateDialog.setNegativeButton("no", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("我要就是不更新");
				dialog.dismiss();
				enterMainActivity();

			}
		});
		updateDialog.show();
	}

	/**
	 * download new Version from net
	 */
	protected void downNewVersion() {
		HttpUtils httpUtils = new HttpUtils();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			httpUtils.download(apkurl, "/mnt/sdcard/" + "Download/mobile.apk",
					new RequestCallBack<File>() {

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							installAPK();
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoading(long total, long current, boolean isUploading) {
							tv_spalsh_plan.setVisibility(View.VISIBLE);//设置控件是否可见
							tv_spalsh_plan.setText(current+"/"+total);//110/200
							super.onLoading(total, current, isUploading);
						}

					});
		}
		

	}

	protected void installAPK() {
		/**
		 *  <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="content" /> //content : 从内容提供者中获取数据  content://
                <data android:scheme="file" /> // file : 从文件中获取数据
                <data android:mimeType="application/vnd.android.package-archive" />
            </intent-filter>
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/mobile.apk")), "application/vnd.android.package-archive");
		startActivityForResult(intent, 0);		

	}

	private void update() {
		new Thread() {

			public void run() {
				long currentTime = System.currentTimeMillis();
				Message msg = Message.obtain();
				try {
					URL url = new URL("http://192.168.2.108:8080/update.html");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(2 * 1000);
					conn.setReadTimeout(5 * 1000);
					conn.setRequestMethod("GET");
					int responseCode = conn.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						InputStream is = conn.getInputStream();
						String json = StreamUtil.parseInpustreamUtil(is);
						// parse Json
						JSONObject jsonObject = new JSONObject(json);
						code = jsonObject.getString("code");
						apkurl = jsonObject.getString("apkurl");
						des = jsonObject.getString("des");
						System.out.println(code + apkurl + des);
						// Determine which is new version
						// double netVersion = Integer.valueOf(code);
						// double currentVersion =
						// Integer.valueOf(getVersion());
						if (code.equals(getVersion())) {
							msg.what = MSG_ENTER_HOME;
						} else {
							msg.what = MSG_UPDATE_DIALOG;

						}
					} else {
						msg.what = MSG_SERVER_ERROR;
					}
				} catch (MalformedURLException e) {
					msg.what = MSG_URL_ERRO;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = MSG_IO_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = MSG_JSON_ERRO;
					e.printStackTrace();
				} finally {
					if ((System.currentTimeMillis() - currentTime) > 2 * 1000) {
						SystemClock.sleep(2 * 1000);
					}
					mHandler.sendMessage(msg);
					
				}
			}
		}.start();

	}

	/**
	 * enter MainActivity
	 */
	protected void enterMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * get current version
	 * 
	 * @return
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			String versionName = pi.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
