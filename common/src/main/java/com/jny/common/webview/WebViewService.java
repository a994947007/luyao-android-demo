package com.jny.common.webview;

import android.content.Context;

import androidx.fragment.app.Fragment;

public interface WebViewService {
    void openWebViewActivity(Context context, String url, boolean isShowActionBar, String title);

    Fragment createFragment(String url, boolean isShowActionBar, String title);
}
