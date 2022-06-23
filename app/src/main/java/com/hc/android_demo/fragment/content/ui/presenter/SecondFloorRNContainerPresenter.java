package com.hc.android_demo.fragment.content.ui.presenter;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.hc.android_demo.R;
import com.hc.support.mvps.Presenter;

public class SecondFloorRNContainerPresenter extends Presenter {
    public static final String COMPONENT_NAME = "myApp";
    private ReactRootView mReactRootView;
    private FrameLayout mRNContainerView;
    private ViewStub contentViewStub;
    private View contentView;

    @Override
    protected void onCreate() {
        super.onCreate();
        mReactRootView = new ReactRootView(getContext());
        mReactRootView.startReactApplication(
                getReactNativeHost().getReactInstanceManager(),
                COMPONENT_NAME,
                null);
    }

    protected ReactNativeHost getReactNativeHost() {
        return ((ReactApplication) getActivity().getApplication()).getReactNativeHost();
    }

    public ReactInstanceManager getReactInstanceManager() {
        return getReactNativeHost().getReactInstanceManager();
    }

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        contentViewStub = rootView.findViewById(R.id.secondContainer);
        if (contentView == null) {
            contentView = contentViewStub.inflate();
            mRNContainerView = contentView.findViewById(R.id.rn_container);
            getReactInstanceManager().onHostResume((Activity) getContext());
            mRNContainerView.addView(mReactRootView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        }
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
}
