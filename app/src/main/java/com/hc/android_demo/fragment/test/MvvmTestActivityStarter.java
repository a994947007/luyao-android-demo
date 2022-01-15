package com.hc.android_demo.activity.test.framework;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.activity.test.view.fragment.MvpsTestFragment;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class MvvmTestActivityStarter implements ActivityStarter {
    @Override
    public String getId() {
        return FragmentConstants.MVPS_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new MvpsTestFragment();
    }
}
