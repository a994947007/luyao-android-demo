package com.hc.android_demo.fragment.content.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hc.android_demo.R;
import com.hc.nested_recycler_fragment.ViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MaterialDesignFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_material_design, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = view.findViewById(R.id.appbar);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_nav);
        // 布局展开时不显示title
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // int totalScrollRange = appBarLayout.getMeasuredHeight() - ViewUtils.dp2px(50);
                int totalScrollRange = appBarLayout.getTotalScrollRange();  // 等价于上述写法
                if (Math.abs(verticalOffset) >= totalScrollRange) {
                    collapsingToolbarLayout.setTitle("折叠布局演示");
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
                bottomNavigationView.setTranslationY(bottomNavigationView.getMeasuredHeight() * Math.abs(verticalOffset) / totalScrollRange);
            }
        });

        List<String> titles = new ArrayList<>();
        titles.add("A");
        titles.add("B");
        titles.add("C");
        titles.add("D");

        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2.setAdapter(new ViewPagerAdapter(getFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();
        viewPager2.setOffscreenPageLimit(4);
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ViewFragment();
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
