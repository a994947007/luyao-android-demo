package com.hc.base.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface ActivityStarter {
    // Fragment对应id
    String getStarterId();
    // 对应Fragment
    @NonNull
    Fragment getContentFragment();
}
