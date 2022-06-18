package com.jny.core;

import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 下载管理器，跨线程
 */
public class DownloadManager {

    private static volatile DownloadManager instance = null;
    private static final String KEY_COMPUTATION_PRIORITY = "service.download";

    private final ScheduledExecutorService SERVICE;

    static {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
    }

    private DownloadManager() {
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
    }

    public static DownloadManager getInstance() {
        return instance;
    }

    public void download(Context context, String url, DownloadCallback callback) {
        DownloadTask downloadTask = new DefaultDownloadTask(context, url, callback);
        SERVICE.submit(downloadTask::download);
    }
}
