package com.jny.react_native;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.hc.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class MyReactApplication extends BaseApplication implements ReactApplication {
    public final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        @Override
        public boolean getUseDeveloperSupport() {
            return true;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            List<ReactPackage> results = new ArrayList<>();
            ServiceLoader<ReactPackage> reactPackages = ServiceLoader.load(ReactPackage.class);
            results.add(new MainReactPackage());
            for (ReactPackage reactPackage : reactPackages) {
                results.add(reactPackage);
            }
            return results;
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }
}
