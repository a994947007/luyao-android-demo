package com.hc.android_demo.fragment.base;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.hc.my_views.SimpleRecyclerView;
import com.hc.util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleRecyclerFragment extends RecyclerFragment{

    private final List<Pair<String, Runnable>> mItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Pair<String, Runnable>> items = bind();
        if (items != null && items.size() > 0) {
            mItems.addAll(items);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = (ViewGroup) inflater.inflate(R.layout.fragment_simple_recycler_container_layout, container, false);
        SimpleRecyclerView homeRecyclerView = mContentView.findViewById(R.id.home_recycler_view);
        homeRecyclerView.setLayoutRes(R.layout.recycler_item);
        homeRecyclerView.setTitleRes(R.id.recycler_item_text_view);
        homeRecyclerView.bind(mItems);
        return mContentView;
    }

    protected List<Pair<String, Runnable>> bind() {
        return null;
    }
}
