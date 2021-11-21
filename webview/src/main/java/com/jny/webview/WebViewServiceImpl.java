package com.jny.webview;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.jny.common.webview.WebViewService;
import com.jny.webview.constants.Constants;
import com.jny.webview.ui.WebViewActivity;
import com.jny.webview.ui.WebViewFragment;

@AutoService({WebViewService.class})
public class WebViewServiceImpl implements WebViewService {
    @Override
    public void openWebViewActivity(Context context, String url, boolean isShowActionBar, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.ACTION_BAR_ENABLE, isShowActionBar);
        context.startActivity(intent);
    }

    @Override
    public Fragment createFragment(String url, boolean isShowActionBar, String title) {
        return WebViewFragment.newInstance(url, isShowActionBar, title);
    }

}
