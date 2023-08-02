package com.hc.my_views.progress;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hc.drawable.ProgressCircleDrawable;

public class ProgressView4 extends View {
    public ProgressView4(Context context) {
        this(context, null);
    }

    public ProgressView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundDrawable(new ProgressCircleDrawable());
    }
}
