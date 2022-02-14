package com.hc.android_demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment;
import com.hc.util.ViewUtils;
import com.jny.webview.webviewProgress.constants.Constants;
import com.jny.webview.webviewProgress.ui.DynamicWebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class DynamicFragment extends SimpleRecyclerFragment {

    private final List<Pair<String, Runnable>> items = new ArrayList<>();

    {
        addItem("动态化activity", this::onClickDynamicWebViewActivity);
    }

    private DynamicFragment() { }

    public static Fragment newInstance() {
        return new DynamicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity)getContext()).getWindow().setNavigationBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
            ((Activity)getContext()).getWindow().setStatusBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
            ((Activity)getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void addItem(String key, Runnable runnable) {
        items.add(new Pair<>(key, runnable));
    }

    @Override
    protected List<Pair<String, Runnable>> bind() {
        return items;
    }

    private void onClickDynamicWebViewActivity() {
        Intent intent = new Intent(getActivity(), DynamicWebViewActivity.class);
        intent.putExtra(Constants.URL, Constants.WEB_URL + "dynamic.html");
        startActivity(intent);
    }

}
