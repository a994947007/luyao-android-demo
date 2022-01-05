package com.hc.android_demo.commands;

import android.content.ComponentName;
import android.content.Intent;

import com.google.auto.service.AutoService;
import com.hc.base.AppEnvironment;
import com.hc.util.ThreadUtils;
import com.jny.common.webview.Command;
import com.jny.common.webview.WebViewCallback;

import java.util.Map;

@AutoService({Command.class})
public class OpenActivityCommand implements Command {
    @Override
    public void execute(Map<String, String> params, WebViewCallback callback) {
        ThreadUtils.runInMainThread(() -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(AppEnvironment.getAppContext(),"com.hc.android_demo.activity.test.view.GridImageLayoutActivity"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppEnvironment.getAppContext().startActivity(intent);
        });
    }

    @Override
    public String getName() {
        return "openActivity";
    }
}
