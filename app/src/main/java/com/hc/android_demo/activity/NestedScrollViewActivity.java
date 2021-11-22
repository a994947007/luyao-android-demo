package com.hc.android_demo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hc.android_demo.R;
import com.hc.nested_recycler_fragment.ViewFragment;

import java.util.ArrayList;
import java.util.List;

public class NestedScrollViewActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll_view);

        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        List<String> titles = new ArrayList<>();
        titles.add("A");
        titles.add("B");
        titles.add("C");
        titles.add("D");
        viewPager2.setAdapter(new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();
        viewPager2.setOffscreenPageLimit(titles.size());

    }

    private static class ViewPager2Adapter extends FragmentStateAdapter {

        public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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