package com.hc.support.rxJava.schedule;

import com.hc.support.rxJava.disposable.Disposable;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class IoScheduler extends Scheduler{

    private static final String KEY_IO_PRIORITY = "rx2.io-priority";  // 优先级
    private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            int priority = Math.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY,
                    Integer.getInteger(KEY_IO_PRIORITY, Thread.NORM_PRIORITY)));
            t.setDaemon(true);
            t.setPriority(priority);
            return t;
        }
    });

    @Override
    public Worker createWorker() {
        return new IoWorker();
    }

    private static class IoWorker extends Scheduler.Worker {
        private Future<?> future;
        private boolean isDisposed = false;

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
            if (delay <= 0) {
                future = SERVICE.submit(run);
            } else {
                future = SERVICE.schedule(run, delay, unit);
            }
            return this;
        }
    }
}
