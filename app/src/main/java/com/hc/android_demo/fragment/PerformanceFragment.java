package com.hc.android_demo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment;
import com.hc.util.ViewUtils;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;
import java.util.List;

public class PerformanceFragment extends SimpleRecyclerFragment {

    private final List<Pair<String, Runnable>> items = new ArrayList<>();

    {
        addItem("包大小优化-颜色选择器", this::onClickAutoChangeColorStyle);
        addItem("RecyclerView局部刷新-item级别", this::onClickRefreshRecyclerItem);
        addItem("RecyclerView局部刷新-element级别", this::onClickRefreshRecyclerElement);
        addItem("RecyclerView局部刷新-element级别（异步）", this::onClickRefreshRecyclerItemCallback);
    }

    private void onClickAutoChangeColorStyle() {
        startContentActivity(FragmentConstants.PERFORMANCE_AUTO_CHANGE_STYLE_ID);
    }

    private void onClickRefreshRecyclerItem() {
        startContentActivity(FragmentConstants.PERFORMANCE_REFRESH_RECYCLER_VIEW_ITEM);
    }

    private void onClickRefreshRecyclerElement() {
        startContentActivity(FragmentConstants.PERFORMANCE_REFRESH_RECYCLER_VIEW_ELEMENT);
    }

    private void onClickRefreshRecyclerItemCallback() {
        startContentActivity(FragmentConstants.PERFORMANCE_REFRESH_RECYCLER_VIEW_ITEM_CALLBACK);
    }

    @Override
    protected void startContentActivity(String id) {
        super.startContentActivity("/" + FragmentConstants.PERFORMANCE + "/" + id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Activity)getContext()).getWindow().setNavigationBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
        ((Activity)getContext()).getWindow().setStatusBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
        ((Activity)getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static PerformanceFragment newInstance() {
        return new PerformanceFragment();
    }

    @Override
    protected List<Pair<String, Runnable>> bind() {
        return items;
    }

    protected void addItem(String key, Runnable runnable) {
        items.add(new Pair<>(key, runnable));
    }
}
