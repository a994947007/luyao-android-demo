package com.jny.webview.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.ViewGroup;

import com.jny.webview.R;
import com.jny.webview.constants.Constants;

public class WebViewActivity extends AppCompatActivity {
    private ViewGroup mFragmentContainer;
    private boolean mIsShowActionBar;
    private String mTitle;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mUrl = getIntent().getStringExtra(Constants.URL);
        mTitle = getIntent().getStringExtra(Constants.TITLE);
        mIsShowActionBar = getIntent().getBooleanExtra(Constants.ACTION_BAR_ENABLE, false);
        bindView();
        bindEvent();
    }

    protected void bindView() {
        mFragmentContainer = findViewById(R.id.webView);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, WebViewFragment.newInstance(mUrl, mIsShowActionBar, mTitle));
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void bindEvent() { }
}