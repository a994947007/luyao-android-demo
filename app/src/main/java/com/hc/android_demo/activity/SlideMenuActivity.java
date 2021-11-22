package com.hc.android_demo.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.hc.recyclerView.ItemBean;
import com.hc.recyclerView.listView.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SlideMenuActivity extends AppCompatActivity {
    private BaseRecyclerView recyclerViewManager;
    private static List<ItemBean> beanList = new ArrayList<>();

    {
        beanList.clear();
        beanList.add(0, new ItemBean("扫一扫", R.drawable.icon_scan_normal));
        beanList.add(1, new ItemBean("关于", R.drawable.icon_abort_normal));
        beanList.add(2, new ItemBean("更多", R.drawable.icon_more_normal));
        beanList.add(3, new ItemBean("设置", R.drawable.icon_setting_normal));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);


        RecyclerView recyclerView = findViewById(R.id.recycler_menu);
        recyclerViewManager = new BaseRecyclerView(this, recyclerView, beanList) {
            @Override
            public int getItemLayoutRes() {
                return R.layout.menu_recycler_item;
            }

            @Override
            public RecyclerView.LayoutManager getLayoutManager() {
                return new LinearLayoutManager(SlideMenuActivity.this, LinearLayoutManager.VERTICAL, false);
            }
        };
        recyclerViewManager.show();
    }


}