package com.hc.android_demo.fragment.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.fragment.content.CustomLayoutManagerFragment;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class CustomLayoutManagerActivityStarter implements ActivityStarter {
    @Override
    public String getId() {
        return FragmentConstants.CUSTOM_LAYOUT_MANAGER_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new CustomLayoutManagerFragment();
    }
}
