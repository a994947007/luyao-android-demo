package com.hc.android_demo.fragment.content.framework.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment;
import com.hc.util.ViewUtils;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.android.demo.plugin.PluginCenter;
import com.jny.common.AppPlugin;
import com.jny.common.DialogPlugin;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;
import java.util.List;

@ARouter(path = FragmentConstants.PLUGIN_TEST_FRAGMENT_ID,
        group = FragmentConstants.FRAMEWORK)
public class PluginTestFragment extends SimpleRecyclerFragment {

    private final List<Pair<String, Runnable>> items = new ArrayList<>();

    {
        addItem("appPlugin", this::onClickAppPlugin);
        addItem("dialogPlugin", this::onClickDialogPlugin);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Activity)getContext()).getWindow().setNavigationBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
        ((Activity)getContext()).getWindow().setStatusBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
        ((Activity)getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void onClickDialogPlugin() {
        PluginCenter.get(DialogPlugin.class).testPlugin();
    }

    private void onClickAppPlugin() {
        PluginCenter.get(AppPlugin.class).testPlugin();
    }

    private void addItem(String key, Runnable runnable) {
        items.add(new Pair<>(key, runnable));
    }

    @Override
    protected List<Pair<String, Runnable>> bind() {
        return items;
    }
}