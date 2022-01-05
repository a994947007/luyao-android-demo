package com.hc.android_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hc.android_demo.fragment.CustomViewFragment;
import com.hc.android_demo.fragment.TestFrameWorkFragment;

public class MainActivity extends AppCompatActivity {

    private int currentItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);     // 由于设置了启动加载bg，因此需要将主题设置回去
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(R.id.navigation_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> setFragment(item.getItemId()));
    }

    @SuppressLint("NonConstantResourceId")
    private boolean setFragment(int fragmentId) {
        Fragment fragment;
        if (currentItemId == fragmentId) {
            return false;
        }
        switch (fragmentId) {
            case R.id.navigation_framework:
                fragment = TestFrameWorkFragment.newInstance();
                break;
            default:
                fragment = CustomViewFragment.newInstance();
                break;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return true;
    }
}