package com.jny.webview.webviewProgress;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.jny.android.demo.arouter_api.RouterManager;
import com.jny.common.webview.WebViewService;
import com.jny.webview.webviewProgress.constants.Constants;
import com.jny.webview.webviewProgress.ui.WebViewFragment;

@AutoService({WebViewService.class})
public class WebViewServiceImpl implements WebViewService {
    @Override
    public void openWebViewActivity(Context context, String url, boolean isShowActionBar, String title) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, url);
        bundle.putString(Constants.TITLE, title);
        bundle.putBoolean(Constants.ACTION_BAR_ENABLE, isShowActionBar);
        RouterManager.getInstance().nav(context, bundle, "/webview/WebViewActivity");
    }

    @Override
    public Fragment createFragment(String url, boolean isShowActionBar, String title) {
        return WebViewFragment.newInstance(url, isShowActionBar, title);
    }

}
