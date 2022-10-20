package com.hc.android_demo.fragment.content.framework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.child.Fragment1;
import com.hc.android_demo.fragment.content.child.Fragment2;
import com.hc.android_demo.fragment.content.child.Fragment3;
import com.hc.android_demo.fragment.content.child.Fragment4;
import com.hc.android_demo.fragment.content.child.Fragment5;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.BaseFragment;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.SCROLL_PRELOAD_TEXT_FRAGMENT_ID,
        group = FragmentConstants.FRAMEWORK)
public class ScrollPreLoadTestFragment extends BaseFragment {

    private final Fragment[] fragments = new Fragment[5];
    private final String[] titles = new String[5];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments[0] = new Fragment1();
        fragments[1] = new Fragment2();
        fragments[2] = new Fragment3();
        fragments[3] = new Fragment4();
        fragments[4] = new Fragment5();

        titles[0] = "Fragment1";
        titles[1] = "Fragment2";
        titles[2] = "Fragment3";
        titles[3] = "Fragment4";
        titles[4] = "Fragment5";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll_preload_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments, titles));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;
        private final String[] titles;

        public SimpleFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, Fragment[] fragments, String[] titles) {
            super(fm, behavior);
            this.fragments = fragments;
            this.titles = titles;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
