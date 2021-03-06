package com.hc.android_demo.fragment.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.fragment.content.framework.fragment.LazyTestFragment;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class LazyActivityStarter implements ActivityStarter {
    @Override
    public String getStarterId() {
        return FragmentConstants.LAZY_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new LazyTestFragment();
    }
}
