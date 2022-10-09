package com.jny.react_native.core;

import androidx.annotation.Nullable;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.jny.android.demo.base_util.AppEnvironment;

import java.util.ArrayList;
import java.util.List;

public class BaseReactNativeHost extends ReactNativeHost {

    private final String mModuleName;

    public BaseReactNativeHost(String moduleName) {
        super(AppEnvironment.getApplication());
        this.mModuleName = moduleName;
    }

    @Override
    public boolean getUseDeveloperSupport() {
        return true;
    }

    @Override
    protected List<ReactPackage> getPackages() {
        List<ReactPackage> results = new ArrayList<>();
        ReactPackage reactPkg = ReactPackageManager.getInstance().getReactPackage(mModuleName);
        results.add(reactPkg);
        results.add(new MainReactPackage());
        return results;
    }

    @Override
    protected String getJSMainModuleName() {
        return "index";
    }

    @Nullable
    @Override
    protected String getBundleAssetName() {
        return mModuleName + ".android.bundle";
    }
}
