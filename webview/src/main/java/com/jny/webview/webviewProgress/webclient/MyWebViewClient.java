package com.jny.webview.webviewProgress.webclient;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    private WebViewClientCallback webViewClientCallback;
    private boolean isStarted = false;
    private boolean isFinished = false;

    public MyWebViewClient(WebViewClientCallback callback) {
        this.webViewClientCallback = callback;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!isStarted) {
            webViewClientCallback.pageViewStarted(url);
        }
        isStarted = true;
        isFinished = false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (isStarted && !isFinished) {
            webViewClientCallback.pageViewFinished(url);
        }
        isFinished = true;
        isStarted = false;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        webViewClientCallback.pageViewOnError();
    }
}
