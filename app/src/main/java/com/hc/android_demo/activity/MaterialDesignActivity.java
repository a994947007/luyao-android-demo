package com.hc.android_demo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hc.android_demo.R;
import com.hc.nested_recycler_fragment.ViewFragment;

import java.util.ArrayList;
import java.util.List;

public class MaterialDesignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design);

        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
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

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager2.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));
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