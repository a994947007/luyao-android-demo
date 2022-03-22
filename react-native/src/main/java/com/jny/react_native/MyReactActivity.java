package com.jny.react_native;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

// TODO 后续换成schema的形式，通过服务端配置，对应的schema跳转到对应的RN bundle包
public class MyReactActivity extends ReactActivity {
    @Nullable
    @Override
    protected String getMainComponentName() {
        return "myApp";
    }
}
