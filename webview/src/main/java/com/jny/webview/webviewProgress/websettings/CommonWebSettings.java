package com.jny.webview.webviewProgress.websettings;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class CommonWebSettings {
    private static volatile CommonWebSettings instance = null;

    static {
        if (instance == null) {
            synchronized (CommonWebSettings.class) {
                if (instance == null) {
                    instance = new CommonWebSettings();
                }
            }
        }
    }

    public static CommonWebSettings getInstance() {
        return instance;
    }

    public void initSettings(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAllowFileAccess(true); // 允许加载本地文件 html
    }
}
