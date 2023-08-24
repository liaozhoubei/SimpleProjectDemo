package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.dao.AntiVirusDao;
import com.example.mobilesafe.utils.MD5Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 病毒扫描
 * @author ASUS-H61M
 *
 */
public class AntivirusActivity extends Activity {
	private ImageView iv_antivirus_scanner;
	private TextView tv_antivirus_text;
	private ProgressBar pb_antivirus_progressbar;
	private LinearLayout ll_antivirus_safeapks;
	private List<String> virus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivirus);
		iv_antivirus_scanner = (ImageView) findViewById(R.id.iv_antivirus_scanner);
		tv_antivirus_text = (TextView) findViewById(R.id.tv_antivirus_text);
		pb_antivirus_progressbar = (ProgressBar) findViewById(R.id.pb_antivirus_progressbar);
		ll_antivirus_safeapks = (LinearLayout) findViewById(R.id.ll_antivirus_safeapks);
		
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1 * 1000);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		rotateAnimation.setInterpolator(linearInterpolator);
		iv_antivirus_scanner.startAnimation(rotateAnimation);
		
		virus = new ArrayList<String>();
		scannerVirus();
	}

	private void scannerVirus() {
		final PackageManager packageManager = getPackageManager();
		tv_antivirus_text.setText("正在初始化64核杀毒引擎....");
		new Thread(){
			public void run() {
				SystemClock.sleep(300);
				List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES );
				pb_antivirus_progressbar.setMax(installedPackages.size());
				int progress = 0;
				
				for(final PackageInfo packageInfo : installedPackages){
					ApplicationInfo applicationInfo = packageInfo.applicationInfo;
					final String appName = applicationInfo.loadLabel(packageManager).toString();
					progress++;
					pb_antivirus_progressbar.setProgress(progress);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							// 获取应用签名信息
							Signature[] signatures = packageInfo.signatures;
							String charsString = signatures[0].toCharsString();
							String signature = MD5Util.passwordMD5(charsString);
							System.out.println(appName + signature);
							
							tv_antivirus_text.setText("扫描：" + appName);	
							TextView scannerText = new TextView(getApplicationContext());
							scannerText.setText(appName);
							
							if (AntiVirusDao.queryAntiVirus(getApplicationContext(), signature)){
								scannerText.setTextColor(Color.RED);
								virus.add(packageInfo.packageName);
							} else {
								scannerText.setTextColor(Color.BLACK);
							}
							
							ll_antivirus_safeapks.addView(scannerText, 0);
						}
					});
					SystemClock.sleep(100);
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						 
						tv_antivirus_text.setText("扫描完成");								
						iv_antivirus_scanner.clearAnimation();
						if (virus.size() > 0 ){
							AlertDialog.Builder builder = new Builder(AntivirusActivity.this);
							builder.setIcon(R.drawable.ic_launcher);
							builder.setTitle("发现" + virus.size() + "个恶意程序");
							builder.setMessage("是否卸载该应用");
							builder.setPositiveButton("确定", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									for (String packageName : virus){
										Intent intent = new Intent();
										intent.setAction("android.intent.action.DELETE");
										intent.addCategory("android.intent.category.DEFAULT");
										intent.setData(Uri.parse("package:" + packageName));
										startActivity(intent);
									}
									
									dialog.dismiss();
								}
							});
							builder.setNegativeButton("取消", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							builder.show();
						}
					}
				});
				
			};
		}.start();
		
		
	}
}
