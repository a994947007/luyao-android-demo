package com.jny.react_native.core;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;

public interface LuReactPackage extends ReactPackage {
    @NonNull
    String getBundleId();
}
