package com.hc.recyclerView.listView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.android_view.R;
import com.hc.recyclerView.ItemBean;
import com.jny.android.demo.base_util.InflaterUtils;

import java.util.List;

public abstract class BaseRecyclerView {
    protected RecyclerView recyclerView;
    protected Context context;
    protected List<ItemBean> itemBeans;
    protected boolean isVertical = true;
    protected boolean isReverse = false;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    public BaseRecyclerView(Context context, RecyclerView recyclerView, List<ItemBean> itemBeans) {
        this.recyclerView = recyclerView;
        this.itemBeans = itemBeans;
        this.context = context;
    }

    public void setOrientation(boolean isVertical, boolean isReverse) {
        this.isVertical = isVertical;
        this.isReverse = isReverse;
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @NonNull
        @Override
        public RecyclerListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View recyclerListItem = InflaterUtils.inflate(context, parent, getItemLayoutRes());
            return new RecyclerListHolder(recyclerListItem);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemBean itemBean = itemBeans.get(position);
            ((RecyclerListHolder)holder).setData(itemBean);
        }

        @Override
        public int getItemCount() {
            return itemBeans.size();
        }
    }

    protected static class RecyclerListHolder extends RecyclerView.ViewHolder {
        public RecyclerListHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(ItemBean itemBean) {
            TextView textView = itemView.findViewById(R.id.textView);
            textView.setText(itemBean.title);
            ImageView imageView = itemView.findViewById(R.id.icon);
            imageView.setImageResource(itemBean.iconResId);
        }
    }

    public void show() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        adapter = createAdapter();
        recyclerView.setAdapter(adapter);
    }

    protected RecyclerView.Adapter<RecyclerView.ViewHolder> createAdapter() {
        return new RecyclerViewAdapter();
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public abstract int getItemLayoutRes();

    public abstract RecyclerView.LayoutManager getLayoutManager();

    private boolean available;

    public boolean available() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
