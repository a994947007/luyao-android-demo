package com.jny.webview.webviewProgress.webclient;

public interface WebViewClientCallback {
    void pageViewStarted(String url);
    void pageViewFinished(String url);
    void pageViewOnError();
    void updateTitle(String title);
}
