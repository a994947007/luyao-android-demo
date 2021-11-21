package com.jny.common.load;

import android.content.Context;
import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hc.support.loadSir.Callback;
import com.jny.common.R;

public class PageErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }

    @Override
    protected void onAttach(Context context, View loadView) {
        super.onAttach(context, loadView);
        SwipeRefreshLayout refreshLayout = loadView.findViewById(R.id.refreshContainer);
        refreshLayout.setOnRefreshListener(() -> {
            reloadListener.onReload(loadView);
            refreshLayout.setRefreshing(false);
        });
    }
}
