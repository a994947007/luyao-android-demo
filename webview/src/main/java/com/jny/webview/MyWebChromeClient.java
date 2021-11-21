package com.jny.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MyWebChromeClient extends WebChromeClient {

    private WebViewClientCallback callback;

    public MyWebChromeClient(WebViewClientCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        this.callback.updateTitle(title);
    }
}
