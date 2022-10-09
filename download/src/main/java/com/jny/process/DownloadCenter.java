package com.jny.process;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.jny.android.demo.base_util.AppEnvironment;
import com.jny.core.DownloadCallback;
import com.jny.process.main.DownloadCallbackManager;

/**
 * 下载中心，跨进程下载
 */
public class DownloadCenter {

    private static volatile DownloadCenter mInstance = null;

    static {
        if (mInstance == null) {
            synchronized (DownloadCenter.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCenter();
                }
            }
        }
    }

    private DownloadCenter() { }

    public static DownloadCenter getInstance() {
        return mInstance;
    }

    public void download(String url, Lifecycle lifecycle, DownloadCallback downloadCallback) {
        String id = DownloadCallbackManager.getInstance().addDownloadCallback(downloadCallback);
        Intent intent = new Intent(AppEnvironment.getAppContext(), DownloadService.class);
        intent.putExtra(DownloadService.URL_KEY, url);
        intent.putExtra(DownloadService.DOWNLOAD_CALLBACK_ID, id);
        DownloadService.enqueueWork(AppEnvironment.getAppContext(), intent);
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    lifecycle.removeObserver(this);
                    DownloadCallbackManager.getInstance().removeDownloadCallback(id);
                }
            }
        });
    }

    public DownloadDisposable download(String url, DownloadCallback downloadCallback) {
        String id = DownloadCallbackManager.getInstance().addDownloadCallback(downloadCallback);
        Intent intent = new Intent(AppEnvironment.getAppContext(), DownloadService.class);
        intent.putExtra(DownloadService.URL_KEY, url);
        intent.putExtra(DownloadService.DOWNLOAD_CALLBACK_ID, id);
        DownloadService.enqueueWork(AppEnvironment.getAppContext(), intent);
        return () -> DownloadCallbackManager.getInstance().removeDownloadCallback(id);
    }
}
