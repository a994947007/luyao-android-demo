package com.jny.android.demo.rxandroid.schedule;

import com.jny.android.demo.rxandroid.disposable.Disposable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewThreadScheduler extends Scheduler{

    /**
     * 在rxJava中，newThread的方式也不是直接使用的new Thread()的来完成，也是用线程池来完成的
     */
    private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    @Override
    public Worker createWorker() {
        return new NewThreadWorker();
    }

    private static class NewThreadWorker extends Scheduler.Worker {

        private boolean isDisposed = false;

        @Override
        public void dispose() {
            if (!isDisposed) {
                isDisposed = true;
                SERVICE.shutdownNow();
            }
        }

        @Override
        public boolean isDisposable() {
            return isDisposed;
        }

        @Override
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            SERVICE.submit(run);
            return this;
        }
    }
}
