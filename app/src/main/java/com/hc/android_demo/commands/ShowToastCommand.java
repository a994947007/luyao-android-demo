package com.hc.android_demo.commands;

import android.widget.Toast;

import com.google.auto.service.AutoService;
import com.hc.base.AppEnvironment;
import com.hc.util.ThreadUtils;
import com.jny.common.webview.Command;
import com.jny.common.webview.WebViewCallback;

import java.util.Map;

@AutoService({Command.class})
public class ShowToastCommand implements Command {
    @Override
    public void execute(Map<String, String> params, WebViewCallback callback) {
        ThreadUtils.runInMainThread(() -> {
            Toast.makeText(AppEnvironment.getAppContext(), params.get("name"), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public String getName() {
        return "showToast";
    }
}
