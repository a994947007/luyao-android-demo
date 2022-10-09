package com.hc.nested_recycler_fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hc.android_view.R;
import com.jny.android.demo.base_util.InflaterUtils;

import java.util.Arrays;
import java.util.List;

public class RecyclerNestedScrollActivity extends AppCompatActivity {
    private RecyclerView headerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_nested_scroll_view);

        /**
         * header，使用一个不能滑动的RecyclerView
         */
        headerView = findViewById(R.id.headerView);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        headerView.setLayoutManager(mLinearLayoutManager);
        headerView.setAdapter(new HeaderAdapter());

        /**
         * body，嵌套滑动
         */
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        List<String> labels = Arrays.asList("linear", "scroll", "recycler");
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), labels));
        tabLayout.setupWithViewPager(viewPager);//此方法就是让tablayout和ViewPager联动
    }

    /********************** header ***********************/
    private static class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.HeaderHolder> {
        private static final String [] itemValues = {
                "header1",
                "header2",
                "header3",
                "header4",
                "header5",
                "header6",
                "header7",
                "header8",
        };

        @Override
        public HeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View recyclerItemView = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_header_item);
            return new HeaderHolder(recyclerItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerNestedScrollActivity.HeaderAdapter.HeaderHolder holder, int position) {
            TextView recyclerItemTextView = holder.itemView.findViewById(R.id.recycler_item);
            recyclerItemTextView.setText(itemValues[position]);
        }

        @Override
        public int getItemCount() {
            return itemValues.length;
        }

        private static class HeaderHolder extends RecyclerView.ViewHolder {
            public HeaderHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    /************************ body *****************************/
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm, List<String> titles) {
            this(fm);
            this.titles = titles;
        }

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new ViewFragment(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}