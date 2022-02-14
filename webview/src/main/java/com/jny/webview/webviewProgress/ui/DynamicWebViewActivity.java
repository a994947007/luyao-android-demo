package com.jny.webview.webviewProgress.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jny.webview.R;
import com.jny.webview.bean.JsParams;
import com.jny.webview.webviewProgress.constants.Constants;
import com.jny.webview.webviewProgress.webclient.WebViewProcessCommandDispatcher;
import com.jny.webview.webviewProgress.websettings.CommonWebSettings;

/**
 * 该Activity可以被动态调起，由服务端下发命令调起，并支持动态js代码
 * 用到一个透明的activity和webView
 */
public class DynamicWebViewActivity extends AppCompatActivity {

    private String mUrl;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_dynamic_web_view);

        WebView webView  = (WebView) findViewById(R.id.dynamic_web_view);
        webView.setBackgroundColor(0);
        webView.getBackground().setAlpha(0);

        Intent intent = getIntent();
        if (intent != null) {
            mUrl = intent.getStringExtra(Constants.URL);
        }
        WebViewProcessCommandDispatcher.getInstance().initAidlConnection();
        CommonWebSettings.getInstance().initSettings(webView);
        webView.addJavascriptInterface(new Bridge(), "customWebView");
        webView.loadUrl(mUrl);
    }

    private class Bridge {
        @JavascriptInterface
        public void executeNativeAction(String jsParams) {
            if (!TextUtils.isEmpty(jsParams)) {
                final JsParams jsParamsObject = new Gson().fromJson(jsParams, JsParams.class);
                if ("closeActivity".equals(jsParamsObject.name)) {
                    finish();
                }
            }
        }
    }
}