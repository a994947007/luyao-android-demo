package com.hc.android_demo.commands;

import com.google.auto.service.AutoService;
import com.hc.util.ActivityUtils;
import com.jny.android.demo.base_util.AppEnvironment;
import com.jny.android.demo.base_util.ThreadUtils;
import com.jny.common.fragment.FragmentConstants;
import com.jny.common.webview.Command;
import com.jny.common.webview.WebViewCallback;

import java.util.Map;

@AutoService({Command.class})
public class OpenActivityCommand implements Command {
    @Override
    public void execute(Map<String, String> params, WebViewCallback callback) {
        ThreadUtils.runInMainThread(() -> {
            ActivityUtils.startContentActivity(AppEnvironment.getAppContext(), FragmentConstants.GRID_IMAGE_LAYOUT_TEST_FRAGMENT_ID);
        });
    }

    @Override
    public String getName() {
        return "openActivity";
    }
}
