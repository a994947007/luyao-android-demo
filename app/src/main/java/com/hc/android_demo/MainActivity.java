package com.hc.android_demo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hc.android_demo.fragment.CustomViewFragment;
import com.hc.android_demo.fragment.DynamicFragment;
import com.hc.android_demo.fragment.PerformanceFragment;
import com.hc.android_demo.fragment.TestFrameWorkFragment;
import com.hc.base.activity.ContentActivityManager;
import com.hc.base.activity.LuActivity;
import com.jny.android.demo.arouter_annotations.ARouter;

import java.util.HashMap;
import java.util.Map;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends LuActivity {

    private int currentItemId = 0;

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);     // 由于设置了启动加载bg，因此需要将主题设置回去
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentActivityManager.getInstance().load();
        setFragment(R.id.navigation_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> setFragment(item.getItemId()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContentActivityManager.getInstance().clear();
    }

    @SuppressLint("NonConstantResourceId")
    private boolean setFragment(int fragmentId) {
        Fragment fragment;
        if (currentItemId == fragmentId) {
            return false;
        }
        fragment = fragmentMap.get(fragmentId);
        if (fragment == null) {
            switch (fragmentId) {
                case R.id.navigation_framework:
                    fragment = TestFrameWorkFragment.newInstance();
                    break;
                case R.id.navigation_web_view:
                    fragment = DynamicFragment.newInstance();
                    break;
                case R.id.navigation_performance:
                    fragment = PerformanceFragment.newInstance();
                    break;
                default:
                    fragment = CustomViewFragment.newInstance();
                    break;
            }
            fragmentMap.put(fragmentId, fragment);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return true;
    }
}