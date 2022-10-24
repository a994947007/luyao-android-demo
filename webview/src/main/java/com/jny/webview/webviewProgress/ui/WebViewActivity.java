package com.jny.webview.webviewProgress.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.ViewGroup;

import com.jny.android.demo.api.Res;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.android.demo.arouter_annotations.Parameter;
import com.jny.android.demo.arouter_api.BundleManager;
import com.jny.webview.R;
import com.jny.webview.webviewProgress.constants.Constants;

@ARouter(path = "WebViewActivity", group = "webview")
public class WebViewActivity extends AppCompatActivity {
    private ViewGroup mFragmentContainer;

    @Parameter("/res/activity_main")
    Res layoutRes;

    @Parameter(Constants.ACTION_BAR_ENABLE)
    boolean mIsShowActionBar;

    @Parameter(Constants.TITLE)
    String mTitle;

    @Parameter(Constants.URL)
    String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        BundleManager.getInstance().bind(this);
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