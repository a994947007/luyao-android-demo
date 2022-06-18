package com.jny.core;

public interface DownloadCallback {
    void onStart();

    void onDownload(float progress);

    void onSuccess(String resultPath);

    void onError(Throwable r);
}
