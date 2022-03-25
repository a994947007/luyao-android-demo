package com.jny.react_native;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;

// TODO 后续换成schema的形式，通过服务端配置，对应的schema跳转到对应的RN bundle包
public class MyReactActivity extends ReactActivity {
    @Nullable
    @Override
    protected String getMainComponentName() {
        return "myApp";
    }
}
