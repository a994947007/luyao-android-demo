package com.jny.react_native.component;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.google.auto.service.AutoService;
import com.jny.react_native.component.edit.CustomReactEditViewManager;

import java.util.ArrayList;
import java.util.List;

@AutoService({ReactPackage.class})
public class ComponentReactPackage implements ReactPackage {
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
        // 这里添加自定义组件
        return results;
    }
}
