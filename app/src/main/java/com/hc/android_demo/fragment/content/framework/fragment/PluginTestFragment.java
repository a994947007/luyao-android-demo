package com.hc.android_demo.fragment.content.framework.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.demo.rxandroid.observer.Consumer;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment;
import com.hc.util.ToastUtils;
import com.hc.util.ViewUtils;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.android.demo.plugin.PluginCenter;
import com.jny.common.AppPlugin;
import com.jny.common.DialogPlugin;
import com.jny.common.PluginModulePlugin;
import com.jny.common.fragment.FragmentConstants;
import com.luyao.android.demo.plugin_module_loader.Callback;
import com.luyao.android.demo.plugin_module_loader.PluginLoader;

import java.util.ArrayList;
import java.util.List;

@ARouter(path = FragmentConstants.PLUGIN_TEST_FRAGMENT_ID,
        group = FragmentConstants.FRAMEWORK)
public class PluginTestFragment extends SimpleRecyclerFragment {

    private final List<Pair<String, Runnable>> items = new ArrayList<>();

    {
        addItem("appPlugin", this::onClickAppPlugin);
        addItem("dialogPlugin", this::onClickDialogPlugin);
        addItem("插件化-pluginModulePlugin", this::onClickPluginModulePlugin);
        addItem("插件化-启动activity组件", this::onClickPluginModuleActivity);
        addItem("插件化-启动activity2组件", this::onClickPluginModuleActivity2);
        addItem("插件化-启动activity3组件", this::onClickPluginModuleActivity3);
        addItem("插件化-启动activity4组件", this::onClickPluginModuleActivity4);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Activity)getContext()).getWindow().setNavigationBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
        ((Activity)getContext()).getWindow().setStatusBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
        ((Activity)getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void onClickPluginModuleActivity4() {
        PluginLoader.loadPlugin("plugin-module")
                .subscribe(aBoolean ->
                                PluginCenter.get(PluginModulePlugin.class).startPluginActivity4(getContext()
                                ),
                        throwable -> ToastUtils.show("show plugin-module plugin error")
                );
    }

    private void onClickPluginModuleActivity3() {
        PluginLoader.loadPlugin("plugin-module")
                .subscribe(aBoolean ->
                                PluginCenter.get(PluginModulePlugin.class).startPluginActivity3(getContext()
                                ),
                        throwable -> ToastUtils.show("show plugin-module plugin error")
                );
    }

    private void onClickPluginModuleActivity2() {
        PluginLoader.loadPlugin("plugin-module")
                .subscribe(aBoolean ->
                                PluginCenter.get(PluginModulePlugin.class).startPluginActivity2(getContext()
                                ),
                        throwable -> ToastUtils.show("show plugin-module plugin error")
                );
    }

    private void onClickPluginModuleActivity() {
        PluginLoader.loadPlugin("plugin-module")
                        .subscribe(aBoolean ->
                                    PluginCenter.get(PluginModulePlugin.class).startPluginActivity(getContext()
                                ),
                                throwable -> ToastUtils.show("show plugin-module plugin error")
                        );
    }

    private void onClickDialogPlugin() {
        PluginCenter.get(DialogPlugin.class).testPlugin();
    }

    private void onClickAppPlugin() {
        PluginCenter.get(AppPlugin.class).testPlugin();
    }

    private void onClickPluginModulePlugin() {
        PluginLoader.loadPlugin("plugin-module", new Callback() {
            @Override
            public void onSuccess() {
                PluginCenter.get(PluginModulePlugin.class).testPlugin();
            }

            @Override
            public void onError(Throwable r) {
                ToastUtils.show("show plugin-module plugin error");
            }
        });
    }

    private void addItem(String key, Runnable runnable) {
        items.add(new Pair<>(key, runnable));
    }

    @Override
    protected List<Pair<String, Runnable>> bind() {
        return items;
    }
}
