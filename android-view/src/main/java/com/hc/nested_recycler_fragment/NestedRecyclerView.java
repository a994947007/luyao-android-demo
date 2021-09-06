package com.hc.nested_recycler_fragment;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NestedRecyclerView extends RecyclerView {
    public NestedRecyclerView(@NonNull Context context) {
        super(context);
    }

    public NestedRecyclerView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedRecyclerView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
    }

}
