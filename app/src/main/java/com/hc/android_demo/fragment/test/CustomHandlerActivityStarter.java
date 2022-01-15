package com.hc.android_demo.fragment.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.fragment.content.CustomHandlerFragment;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class CustomHandlerActivityStarter implements ActivityStarter {
    @Override
    public String getId() {
        return FragmentConstants.CUSTOM_TEST_HANDLER_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new CustomHandlerFragment();
    }
}
