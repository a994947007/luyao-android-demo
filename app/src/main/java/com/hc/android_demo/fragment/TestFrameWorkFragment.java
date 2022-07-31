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
import com.hc.base.activity.Constants;
import com.hc.base.activity.ContentActivity;
import com.hc.util.ViewUtils;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;
import java.util.List;

public class TestFrameWorkFragment extends SimpleRecyclerFragment {

    private final List<Pair<String, Runnable>> items = new ArrayList<>();

    {
        addItem("自定义Mvps使用测试", this::onClickMvpsActivity);
        addItem("自定义Handler使用测试", this::onClickCustomHandlerActivity);
        addItem("老版本MVP的使用", this::onClickTestMvpActivity);
        addItem("MVVM+Databinding的使用", this::onClickMvvmDataBindingActivity);
        addItem("滑动预加载Fragment", this::onScrollPreloadFragment);
        addItem("图片下载+跨进程Binder通信+文件下载", this::onClickDownloadBitmapFragment);
        addItem("post、postOnAnimation原理", this::onClickTextViewPostFragment);
        addItem("KT DSL、高阶函数", this::onClickDSLTestFragment);
    }

    private TestFrameWorkFragment() { }

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

    private void onClickDSLTestFragment() {
        startContentActivity(FragmentConstants.KT_DSL_TEST_FRAGMENT_ID);
    }

    private void onClickTextViewPostFragment() {
        startContentActivity(FragmentConstants.VIEW_POST_TEST_FRAGMENT_ID);
    }

    private void onClickDownloadBitmapFragment() {
        startContentActivity(FragmentConstants.DOWNLOAD_BITMAP_TEST_FRAGMENT_ID);
    }

    private void onClickMvpsActivity() {
        startContentActivity(FragmentConstants.MVPS_TEST_FRAGMENT_ID);
    }

    private void onClickCustomHandlerActivity() {
        startContentActivity(FragmentConstants.CUSTOM_TEST_HANDLER_ID);
    }

    private void onClickMvvmDataBindingActivity() {
        startContentActivity(FragmentConstants.MVVM_TEST_FRAGMENT_ID);
    }

    @Override
    protected void startContentActivity(String id) {
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(Constants.CONTENT_FRAGMENT_KEY, id);
        startActivity(intent);
    }

    private void onClickTestMvpActivity() {
        startContentActivity(FragmentConstants.MVP_TEST_FRAGMENT_ID);
    }

    private void addItem(String key, Runnable runnable) {
        items.add(new Pair<>(key, runnable));
    }

    private void onScrollPreloadFragment() {
        startContentActivity(FragmentConstants.SCROLL_PRELOAD_TEXT_FRAGMENT_ID);
    }
}
