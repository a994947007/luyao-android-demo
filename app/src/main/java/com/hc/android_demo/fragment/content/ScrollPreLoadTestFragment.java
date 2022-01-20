package com.hc.android_demo.fragment.content;

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
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.BaseFragment;
import com.hc.support.preload.PreloadManager;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class ScrollPreLoadTestFragment extends BaseFragment implements ActivityStarter {

    private Fragment fragments[] = new Fragment[3];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments[0] = new Fragment1();
        fragments[1] = new Fragment2();
        fragments[2] = new Fragment3();
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
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));
        viewPager.setOffscreenPageLimit(1);
        PreloadManager.getInstance().register(viewPager, fragments);
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.SCROLL_PRELOAD_TEXT_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new ScrollPreLoadTestFragment();
    }

    private static class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public SimpleFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, Fragment[] fragments) {
            super(fm, behavior);
            this.fragments = fragments;
        }

        public Fragment[] getFragments() {
            return fragments;
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
    }
}
