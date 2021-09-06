package com.hc.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadUtils {
    private static final ExecutorService executorService =
            new ThreadPoolExecutor(8 ,16,  60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static void runOnAsyncTread(Runnable runnable) {
        executorService.submit(runnable);
    }

    public static void runInMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void runOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
