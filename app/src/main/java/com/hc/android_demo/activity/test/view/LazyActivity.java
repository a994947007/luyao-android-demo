package com.hc.android_demo.activity.test.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hc.android_demo.R;
import com.hc.viewPager_fragment.ConcreteFragment;

import java.util.ArrayList;
import java.util.List;

public class LazyActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private static final List<String> titleList = new ArrayList<>();
    private static final List<Fragment> fragmentList = new ArrayList<>();

    static {
        fragmentList.add(new ConcreteFragment());
        fragmentList.add(new ConcreteFragment());
        fragmentList.add(new ConcreteFragment());
        fragmentList.add(new ConcreteFragment());
        fragmentList.add(new ConcreteFragment());

        titleList.add("1");
        titleList.add("2");
        titleList.add("3");
        titleList.add("4");
        titleList.add("5");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_activitiy);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}