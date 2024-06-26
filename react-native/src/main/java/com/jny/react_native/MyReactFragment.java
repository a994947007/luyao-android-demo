package com.jny.react_native;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.google.auto.service.AutoService;
import com.hc.base.activity.ActivityStarter;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.REACT_NATIVE_FRAGMENT_ID,
        group = FragmentConstants.DYNAMIC)
public class MyReactFragment extends Fragment {

    public static final String COMPONENT_NAME = "myApp";
    private ReactRootView mReactRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Activity)getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mReactRootView = new ReactRootView(context);
        mReactRootView.startReactApplication(
                getReactNativeHost().getReactInstanceManager(),
                COMPONENT_NAME,
                null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mReactRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getReactInstanceManager().onHostResume((Activity) getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        getReactInstanceManager().onHostPause((Activity) getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
            mReactRootView = null;
        }
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostDestroy(getActivity()
            );
        }
    }

    protected ReactNativeHost getReactNativeHost() {
        return ((ReactApplication) getActivity().getApplication()).getReactNativeHost();
    }

    public ReactInstanceManager getReactInstanceManager() {
        return getReactNativeHost().getReactInstanceManager();
    }
}
