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
import com.hc.android_demo.activity.test.framework.CustomHandlerActivity;
import com.hc.android_demo.activity.test.framework.MvpsActivity;
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment;
import com.hc.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class TestFrameWorkFragment extends SimpleRecyclerFragment {

    private final List<Pair<String, Runnable>> items = new ArrayList<>();

    {
        addItem("自定义Mvps使用测试", this::onClickMvpsActivity);
        addItem("自定义Handler使用测试", this::onClickCustomHandlerActivity);
    }

    private TestFrameWorkFragment() {}

    public static Fragment newInstance() {
        return new TestFrameWorkFragment();
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

    @Override
    protected List<Pair<String, Runnable>> bind() {
        return items;
    }

    private void onClickMvpsActivity() {
        startActivity(new Intent(getActivity(), MvpsActivity.class));
    }

    private void onClickCustomHandlerActivity() {
        startActivity(new Intent(getActivity(), CustomHandlerActivity.class));
    }

    private void addItem(String key, Runnable runnable) {
        items.add(new Pair<>(key, runnable));
    }
}
