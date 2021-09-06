package com.hc.my_views.nestedScrollVIew;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.core.view.NestedScrollingParent;

public class NestedScrollViewParent extends FrameLayout implements NestedScrollingParent {
    public NestedScrollViewParent(Context context) {
        super(context);
    }

    public NestedScrollViewParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollViewParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
