package com.hc.android_demo.fragment.content.dynamic.reactpackage;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.google.auto.service.AutoService;
import com.jny.react_native.component.bottomsheet.ReactBottomSheetLayoutManager;
import com.jny.react_native.component.edit.CustomReactEditViewManager;
import com.jny.react_native.component.wrapedit.WrapReactEditViewManager;
import com.jny.react_native.core.LuReactPackage;

import java.util.ArrayList;
import java.util.List;

@AutoService({LuReactPackage.class})
public class SecondFloorReactPackage implements LuReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        // 这里添加RNBridge
        return new ArrayList<>(0);
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        List<ViewManager> results = new ArrayList<>();
        results.add(new CustomReactEditViewManager());
        results.add(new WrapReactEditViewManager());
        results.add(new ReactBottomSheetLayoutManager());
        // 这里添加自定义组件
        return results;
    }

    @NonNull
    @Override
    public String getBundleId() {
        return "index";
    }
}
