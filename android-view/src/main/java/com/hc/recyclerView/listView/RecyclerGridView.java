package com.hc.recyclerView.listView;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.android_view.R;
import com.hc.recyclerView.ItemBean;

import java.util.List;

public class RecyclerGridView extends BaseRecyclerView {

    public RecyclerGridView(Context context, RecyclerView recyclerView, List<ItemBean> itemBeans) {
        super(context, recyclerView, itemBeans);
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.recycler_grid_item;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(context, 3,
                isVertical? LinearLayoutManager.VERTICAL:LinearLayoutManager.HORIZONTAL,
                isReverse);
    }

}
