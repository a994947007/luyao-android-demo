package com.jny.webview.webviewProgress.bridge;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.jny.webview.bean.JsParams;
import com.jny.webview.webviewProgress.webclient.WebViewProcessCommandDispatcher;

public class JsBridge {

    @JavascriptInterface
    public void executeNativeAction(String jsParams) {
        Log.d("JsBridge", jsParams);
        if (!TextUtils.isEmpty(jsParams)) {
            final JsParams jsParamsObject = new Gson().fromJson(jsParams, JsParams.class);
            WebViewProcessCommandDispatcher.getInstance().executeCommand(jsParamsObject.name, new Gson().toJson(jsParamsObject.params));
        }
    }
}
