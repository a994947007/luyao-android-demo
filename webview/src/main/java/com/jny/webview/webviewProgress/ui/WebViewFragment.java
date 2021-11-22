package com.jny.webview.webviewProgress.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hc.design.actionbar.CommonActionBar;
import com.hc.support.loadSir.LoadService;
import com.hc.support.loadSir.LoadSir;
import com.jny.common.load.LoadingCallback;
import com.jny.common.load.PageErrorCallback;
import com.jny.webview.R;
import com.jny.webview.webviewProgress.base.BaseWebView;
import com.jny.webview.webviewProgress.constants.Constants;
import com.jny.webview.webviewProgress.webclient.WebViewClientCallback;

public class WebViewFragment extends Fragment implements WebViewClientCallback {
    private String mUrl;
    private CommonActionBar mCommonActionBar;
    private boolean mIsShowActionBar;
    private String mTitle;
    private BaseWebView mWebView;
    private LoadService loadService;
    private ViewGroup contentView;
    private boolean actionBarShowing;
    private boolean isError;
    private SwipeRefreshLayout refreshLayout;

    public static Fragment newInstance(String url, boolean isShowActionBar, String title) {
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.URL, url);
        bundle.putBoolean(Constants.ACTION_BAR_ENABLE, isShowActionBar);
        bundle.putString(Constants.TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUrl = bundle.getString(Constants.URL);
            mIsShowActionBar = bundle.getBoolean(Constants.ACTION_BAR_ENABLE);
            mTitle = bundle.getString(Constants.TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.layout_web_view, container, false);
        mWebView = contentView.findViewById(R.id.webView);

        loadService = LoadSir.register(contentView, view -> {
            mWebView.reload();
        });
        loadService.showCallback(LoadingCallback.class);
        mWebView.registerWebViewCallback(this);
        mCommonActionBar = new CommonActionBar();
        if (mIsShowActionBar && !actionBarShowing) {
            mCommonActionBar.addActionBar(contentView);
            if (!TextUtils.isEmpty(mTitle)) {
                mCommonActionBar.setTitle(mTitle);
            }
            mCommonActionBar.setLeftClickListener(v -> ((Activity)getContext()).finish());
            actionBarShowing = true;
        }
        refreshLayout = contentView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                refreshLayout.setRefreshing(false);
            }
        });

        return loadService.getLoadLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void pageViewStarted(String url) {
        isError = false;
        loadService.showCallback(LoadingCallback.class);
    }

    @Override
    public void pageViewFinished(String url) {
        if (isError) {
            loadService.showCallback(PageErrorCallback.class);
        } else {
            loadService.showSuccess();
        }
    }

    @Override
    public void pageViewOnError() {
        isError = true;
    }

    @Override
    public void updateTitle(String title) {
        mCommonActionBar.setTitle(title);
    }
}
