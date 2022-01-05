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
        RecyclerView homeRecyclerView = mContentView.findViewById(R.id.home_recycler_view);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        homeRecyclerView.setLayoutManager(mLinearLayoutManager);
        homeRecyclerView.setAdapter(new SimpleRecyclerAdapter(mItems));
        return mContentView;
    }

    private static class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerViewHolder> {
        private final List<Pair<String, Runnable>> items = new ArrayList<>();

        public SimpleRecyclerAdapter(List<Pair<String, Runnable>> items) {
            this.items.addAll(items);
        }

        @NonNull
        @Override
        public SimpleRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_item);
            return new SimpleRecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleRecyclerViewHolder holder, int position) {
            TextView itemTextView = holder.itemView.findViewById(R.id.recycler_item_text_view);
            itemTextView.setText(items.get(position).first);
            holder.itemView.setOnClickListener(v -> items.get(position).second.run());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class SimpleRecyclerViewHolder extends RecyclerView.ViewHolder {

        public SimpleRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected List<Pair<String, Runnable>> bind() {
        return null;
    }
}
