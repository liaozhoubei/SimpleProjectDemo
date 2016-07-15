package com.example.mobilesafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;

public class MyApplication extends Application {
	private void onCreat() {
		Thread.currentThread().setUncaughtExceptionHandler(new MyUnacaughtExceptionhandle());
	}
	/**
	 * 用于捕获未发现的异常
	 * @author ASUS-H61M
	 *
	 */
	private class MyUnacaughtExceptionhandle implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			ex.printStackTrace();
			try {
				//将捕获到异常,保存到SD卡中
				ex.printStackTrace(new PrintStream(new File("/mnt/sdcard/log.txt")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//myPid() : 获取当前应用程序的进程id
			//自己把自己杀死(造成闪退的原因)
			android.os.Process.killProcess(android.os.Process.myPid());
		
		}
		
	}
}
