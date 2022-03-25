package com.jny.react_native;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.google.auto.service.AutoService;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class MyReactFragment extends Fragment implements ActivityStarter {

    public static final String COMPONENT_NAME = "myApp";
    private ReactRootView mReactRootView;

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
        super.onCreateView(inflater, container, savedInstanceState);
        getReactInstanceManager().onHostResume((Activity) getContext());
        return mReactRootView;
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

    @Override
    public String getStarterId() {
        return FragmentConstants.REACT_NATIVE_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new MyReactFragment();
    }
}
