package com.jny.webview.webviewProgress.bridge;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jny.webview.bean.JsParams;
import com.jny.webview.webviewProgress.webclient.WebViewProcessCommandDispatcher;

import java.lang.ref.WeakReference;

public class JsBridge {
    private WeakReference<WebView> webViewRef;

    public JsBridge(WebView webView) {
        this.webViewRef = new WeakReference<>(webView);
    }

    @JavascriptInterface
    public void executeNativeAction(String jsParams) {
        Log.d("JsBridge", jsParams);
        if (!TextUtils.isEmpty(jsParams)) {
            final JsParams jsParamsObject = new Gson().fromJson(jsParams, JsParams.class);
            if (jsParamsObject.params != null && !TextUtils.isEmpty(jsParamsObject.params.get("callbackName"))) {
                String jsCallbackName = jsParamsObject.params.get("callbackName");
                WebViewProcessCommandDispatcher.getInstance().executeCommandWithCallback(jsParamsObject.name, new Gson().toJson(jsParamsObject.params), new WebViewResponseCallback() {
                    @Override
                    public void onResult(String response) {
                        WebView webView = webViewRef.get();
                        if (webView != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                webView.post(() -> {
                                    webView.evaluateJavascript("javascript:config.callback('" + jsCallbackName + "'," + response +")", null);
                                });
                            }
                        }
                    }
                });
            } else {
                WebViewProcessCommandDispatcher.getInstance().executeCommand(jsParamsObject.name, new Gson().toJson(jsParamsObject.params));
            }
        }
    }
}
