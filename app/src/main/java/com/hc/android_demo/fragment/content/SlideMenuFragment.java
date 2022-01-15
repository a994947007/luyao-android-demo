package com.hc.android_demo.fragment.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.hc.recyclerView.ItemBean;
import com.hc.recyclerView.listView.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SlideMenuFragment extends Fragment {

    private BaseRecyclerView recyclerViewManager;
    private static List<ItemBean> beanList = new ArrayList<>();

    {
        beanList.clear();
        beanList.add(0, new ItemBean("扫一扫", R.drawable.icon_scan_normal));
        beanList.add(1, new ItemBean("关于", R.drawable.icon_abort_normal));
        beanList.add(2, new ItemBean("更多", R.drawable.icon_more_normal));
        beanList.add(3, new ItemBean("设置", R.drawable.icon_setting_normal));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slide_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerViewManager = new BaseRecyclerView(getContext(), recyclerView, beanList) {
            @Override
            public int getItemLayoutRes() {
                return R.layout.menu_recycler_item;
            }

            @Override
            public RecyclerView.LayoutManager getLayoutManager() {
                return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            }
        };
        recyclerViewManager.show();
    }
}
