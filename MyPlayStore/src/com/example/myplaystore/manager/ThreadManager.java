package com.example.myplaystore.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
/**
 * 线程池
 * @author ASUS-H61M
 *
 */
public class ThreadManager {
	private static ThreadPool mThreadPool;

	public static ThreadPool getThreadPool() {
		if (mThreadPool == null) {
			synchronized (ThreadManager.class) {
				if (mThreadPool == null) {
					int cpuCount = Runtime.getRuntime().availableProcessors();
					int corePoolsize = cpuCount * 2 + 1;
					mThreadPool = new ThreadPool(corePoolsize, corePoolsize, 2l);
				}
			}
		}
		return mThreadPool;
	}

	public static class ThreadPool {
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;
		private ThreadPoolExecutor poolExecutor;

		private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		public void execute(Runnable r) {
			if (poolExecutor == null) {
				/**
				 * TimeUnit.SECONDS : 设置keepAliveTime休息时间的单位
				 */
				poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
						new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(), new AbortPolicy());

			}
			poolExecutor.execute(r);
		}
		
		public void cancel(Runnable r) {
			if (poolExecutor != null) {
				poolExecutor.getQueue().remove(r);
			}
		}

	}
}
