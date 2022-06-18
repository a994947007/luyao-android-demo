package com.jny.process;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.jny.core.DownloadCallback;
import com.jny.core.DownloadManager;
import com.jny.process.remote.DownloadCallbackDispatcher;

/**
 * 该Service运行在一个独立的进程
 */
public class DownloadService extends JobIntentService {
    private static final int JOB_ID = 1001;
    public static final String URL_KEY = "URK_KEY";
    public static final String DOWNLOAD_CALLBACK_ID = "DOWNLOAD_CALLBACK_ID";

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DownloadService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String url = intent.getStringExtra(URL_KEY);
        String id = intent.getStringExtra(DOWNLOAD_CALLBACK_ID);
        if (!DownloadCallbackDispatcher.getInstance().isInit()) {
            DownloadCallbackDispatcher.getInstance().initConnection();
        }
        DownloadManager.getInstance().download(this, url, new DownloadCallback() {
            @Override
            public void onStart() {
                DownloadCallbackDispatcher.getInstance().onStart(id);
            }

            @Override
            public void onDownload(float progress) {
                DownloadCallbackDispatcher.getInstance().onDownload(id, progress);
            }

            @Override
            public void onSuccess(String resultPath) {
                DownloadCallbackDispatcher.getInstance().onSuccess(id, resultPath);
            }

            @Override
            public void onError(Throwable r) {
                DownloadCallbackDispatcher.getInstance().onError(id, r);
            }
        });
    }
}
