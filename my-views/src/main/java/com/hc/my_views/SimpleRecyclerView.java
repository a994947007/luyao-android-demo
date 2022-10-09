package com.hc.my_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jny.android.demo.base_util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现了一个简单列表的RecyclerView，只需要设置recyclerView的item layout和对应title的id即可
 */
public class SimpleRecyclerView extends RecyclerView {

    private int layoutRes;
    private int titleRes;

    private SimpleRecyclerAdapter mAdapter;

    public SimpleRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public void setTitleRes(@IdRes int titleRes) {
        this.titleRes = titleRes;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void bind(List<Pair<String, Runnable>> items) {
        if (mAdapter == null) {
            RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            this.setLayoutManager(mLinearLayoutManager);
            mAdapter = new SimpleRecyclerAdapter(items, layoutRes, titleRes);
            setAdapter(mAdapter);
        } else {
            mAdapter.update(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private static class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerViewHolder> {
        private final List<Pair<String, Runnable>> items = new ArrayList<>();

        private final int layoutRes;
        private final int titleRes;

        public SimpleRecyclerAdapter(List<Pair<String, Runnable>> items, @LayoutRes int layoutRes, @IdRes int titleRes) {
            this.items.addAll(items);
            this.layoutRes = layoutRes;
            this.titleRes = titleRes;
        }

        @NonNull
        @Override
        public SimpleRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = InflaterUtils.inflate(parent.getContext(), parent, layoutRes);
            return new SimpleRecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleRecyclerViewHolder holder, int position) {
            TextView itemTextView = holder.itemView.findViewById(titleRes);
            itemTextView.setText(items.get(position).first);
            holder.itemView.setOnClickListener(v -> {
                if (items.get(position).second == null) {
                    return;
                }
                items.get(position).second.run();
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void update(List<Pair<String, Runnable>> items) {
            this.items.clear();
            this.items.addAll(items);
        }
    }

    private static class SimpleRecyclerViewHolder extends RecyclerView.ViewHolder {

        public SimpleRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
