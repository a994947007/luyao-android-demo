package com.jny.common.webview;

public interface DownloadCallback {
    void onStart();

    void onError(String err);

    void onSuccess();
}
