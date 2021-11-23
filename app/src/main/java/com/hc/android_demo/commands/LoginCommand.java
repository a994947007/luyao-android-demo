package com.hc.android_demo.commands;

import android.text.TextUtils;
import android.util.Log;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.jny.common.webview.Command;
import com.jny.common.webview.WebViewCallback;

import java.util.HashMap;
import java.util.Map;

@AutoService({Command.class})
public class LoginCommand implements Command {
    @Override
    public void execute(Map<String, String> params, WebViewCallback webViewCallback) {
        Log.d("LoginCommand", params.get("username"));
        Map<String, String> map = new HashMap<>();
        if (TextUtils.equals(params.get("username"), "123") && TextUtils.equals("password", "123")) {
            map.put("result", "登录成功");
        } else {
            map.put("result", "登录失败");
        }
        webViewCallback.onResult(new Gson().toJson(map));
    }

    @Override
    public String getName() {
        return "login";
    }
}
