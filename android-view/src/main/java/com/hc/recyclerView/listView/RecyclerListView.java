package com.hc.recyclerView.listView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.android_view.R;
import com.hc.recyclerView.ItemBean;
import com.hc.util.InflaterUtils;

import java.util.List;

public class RecyclerListView extends BaseRecyclerView{

    // item有两种，一种是正常的item，一种是显示加载动画或者加载失败的item
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_LOAD = 2;

    private OnRefreshListener refreshListener;

    public RecyclerListView(Context context, RecyclerView recyclerView, List<ItemBean> itemBeans) {
        super(context, recyclerView, itemBeans);
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.recycler_list_item;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(context,
                isVertical? LinearLayoutManager.VERTICAL:LinearLayoutManager.HORIZONTAL,
                isReverse);
    }

    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_LOAD) {
                View view = InflaterUtils.inflate(context, parent, R.layout.recycler_list_item_laod_more);
                return new RecyclerLoadHolder(view);
            } else {
                View view = InflaterUtils.inflate(context, parent, R.layout.recycler_list_item);
                return new RecyclerListHolder(view);
            }
        }

        // update需要放到onBind中，不能放在onCreate中，由于缓存问题，并不会在每次滑动加载新的item时onCreate，而是去缓存中查找，而onBind会在滑动的过程中被调用，不会受缓存的影响
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_LOAD) {
                ((RecyclerLoadHolder) holder).update(RecyclerLoadHolder.LOADING);
            } else {
                ((RecyclerListHolder) holder).setData(itemBeans.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == itemBeans.size() - 1) {
                return TYPE_LOAD;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return itemBeans.size();
        }
    }

    @Override
    protected RecyclerView.Adapter<RecyclerView.ViewHolder> createAdapter() {
        return new RecyclerListAdapter();
    }

    /**
     * 底部的刷新item
     */
    public class RecyclerLoadHolder extends RecyclerView.ViewHolder {
        public static final int LOADING = 1;
        public static final int LOAD_FAIlER = 2;
        public static final int NORMAL = 3;
        private final View mProgressView;
        private final View mTextView;

        public RecyclerLoadHolder(@NonNull View itemView) {
            super(itemView);
            mProgressView = itemView.findViewById(R.id.progress_circular);
            mTextView = itemView.findViewById(R.id.text);
            mTextView.setOnClickListener(v -> {
                if (refreshListener != null) {
                    update(LOADING);
                    refreshListener.onRefresh(this);
                }
            });
        }

        public void update(int type) {
            mProgressView.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);

            if (type == LOADING) {
                mProgressView.setVisibility(View.VISIBLE);
                if (refreshListener != null) {
                    refreshListener.onRefresh(this);
                }
            } else if (type == LOAD_FAIlER) {
                mTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRefreshListener(OnRefreshListener listener) {
        this.refreshListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh(RecyclerLoadHolder holder);
    }

}
