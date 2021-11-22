package com.jny.webview.webviewProgress.webclient;

import android.util.Log;
import android.webkit.ConsoleMessage;
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

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d("MyWebChromeClient", consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
    }
}
