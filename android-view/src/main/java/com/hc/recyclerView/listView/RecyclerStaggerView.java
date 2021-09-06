package com.hc.recyclerView.listView;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.hc.android_view.R;
import com.hc.recyclerView.ItemBean;

import java.util.List;

public class RecyclerStaggerView extends BaseRecyclerView{
    public RecyclerStaggerView(Context context, RecyclerView recyclerView, List<ItemBean> itemBeans) {
        super(context, recyclerView, itemBeans);
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.recycler_stagger_item;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                isVertical? LinearLayoutManager.VERTICAL:LinearLayoutManager.HORIZONTAL);
        staggeredGridLayoutManager.setReverseLayout(isReverse);
        return staggeredGridLayoutManager;
    }
}
