package com.hc.base.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.hc.base.fragment.BaseFragment;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment) {
                if (((BaseFragment) fragment).onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
