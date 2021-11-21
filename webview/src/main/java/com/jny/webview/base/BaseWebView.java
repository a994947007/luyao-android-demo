package com.jny.webview.base;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.hc.base.AppEnvironment;
import com.jny.webview.bridge.JsBridge;
import com.jny.webview.webclient.MyWebChromeClient;
import com.jny.webview.webclient.MyWebViewClient;
import com.jny.webview.webclient.WebViewClientCallback;
import com.jny.webview.websettings.CommonWebSettings;

public class BaseWebView extends WebView {
    public BaseWebView(@NonNull Context context) {
        this(context, null);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        CommonWebSettings.getInstance().initSettings(this);
        addJavascriptInterface(new JsBridge(), "customWebView");
    }

    public void registerWebViewCallback(WebViewClientCallback webViewClientCallback) {
        setWebViewClient(new MyWebViewClient(webViewClientCallback));
        setWebChromeClient(new MyWebChromeClient(webViewClientCallback));
    }
}
