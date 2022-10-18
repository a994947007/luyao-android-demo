package com.jny.webview.webviewProgress;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.jny.android.demo.arouter_api.BundleBuilder;
import com.jny.android.demo.arouter_api.RouterManager;
import com.jny.common.webview.WebViewService;
import com.jny.webview.webviewProgress.constants.Constants;
import com.jny.webview.webviewProgress.ui.WebViewFragment;

@AutoService({WebViewService.class})
public class WebViewServiceImpl implements WebViewService {
    @Override
    public void openWebViewActivity(Context context, String url, boolean isShowActionBar, String title) {
        RouterManager.getInstance().nav(context,
                "/webview/WebViewActivity",
                "webview",
                new BundleBuilder()
                        .withString(Constants.URL, url)
                        .withString(Constants.TITLE, title)
                        .withBoolean(Constants.ACTION_BAR_ENABLE, isShowActionBar));
    }

    @Override
    public Fragment createFragment(String url, boolean isShowActionBar, String title) {
        return WebViewFragment.newInstance(url, isShowActionBar, title);
    }

}
