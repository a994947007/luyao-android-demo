package com.hc.support.rxJava.schedule;

import android.annotation.SuppressLint;

import com.hc.support.rxJava.disposable.Disposable;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ComputationScheduler extends Scheduler{

    /**
     * 在RxJava2中由于ScheduledThreadPoolExecutor未提供设置maxsize的方法，作者自己加了限制用一个固定大小的work队列来完成的
     * 我们简单起见，用反射来设置
     */
    private static final ScheduledExecutorService SERVICE;

    private static final String KEY_MAX_THREADS = "my.rx2.computation-threads"; // 最大线程数
    private static final String KEY_COMPUTATION_PRIORITY = "rx2.computation-priority";  // 优先级

    static int cap(int cpuCount, int paramThreads) {
        return paramThreads <= 0 || paramThreads > cpuCount ? cpuCount : paramThreads;
    }

    static {
        SERVICE = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                int priority = Math.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY,
                        Integer.getInteger(KEY_COMPUTATION_PRIORITY, Thread.NORM_PRIORITY)));
                t.setDaemon(true);
                t.setPriority(priority);
                return t;
            }
        });
/*        try {
            @SuppressLint("SoonBlockedPrivateApi") Field maximumPoolSize = ThreadPoolExecutor.class.getDeclaredField("maximumPoolSize");
            maximumPoolSize.setAccessible(true);
            int maxSize = cap(Runtime.getRuntime().availableProcessors(), Integer.getInteger(KEY_MAX_THREADS, 1));
            maximumPoolSize.set(SERVICE, maxSize);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public Worker createWorker() {
        return new ComputationWorker();
    }

    private static class ComputationWorker extends Scheduler.Worker {
        private boolean isDisposed = false;
        private Future<?> future;

        @Override
        public void dispose() {
            if (!isDisposed) {
                isDisposed = true;
                future.cancel(true);
            }
        }

        @Override
        public boolean isDisposable() {
            return isDisposed;
        }

        @Override
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            if (delay == 0) {
                future = SERVICE.submit(run);
            } else {
                future = SERVICE.schedule(run, delay, unit);
            }
            return this;
        }
    }
}
