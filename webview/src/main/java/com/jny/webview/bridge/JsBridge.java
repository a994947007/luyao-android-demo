package com.jny.webview.bridge;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hc.base.AppEnvironment;
import com.jny.webview.bean.JsParams;

public class JsBridge {

    @JavascriptInterface
    public void executeNativeAction(String jsParams) {
        Log.d("JsBridge", jsParams);
        if (TextUtils.isEmpty(jsParams)) {
            return;
        }
        Gson gson = new Gson();
        JsParams params = gson.fromJson(jsParams, JsParams.class);
        Toast.makeText(AppEnvironment.getAppContext(), params.params, Toast.LENGTH_SHORT).show();
    }
}
