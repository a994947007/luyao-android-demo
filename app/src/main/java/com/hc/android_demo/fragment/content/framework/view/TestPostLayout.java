package com.hc.android_demo.fragment.content.framework.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hc.android_demo.R;


public class TestPostLayout extends LinearLayout {

    private TextView mHandlerPostTextTv;
    private Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public TestPostLayout(Context context) {
        super(context);
    }

    public TestPostLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestPostLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHandlerPostTextTv = findViewById(R.id.test_attached_handler_post_tv);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MAIN_HANDLER.post(() -> mHandlerPostTextTv.setText(mHandlerPostTextTv.getText() + "\nw:" + getWidth() + " h:" + getHeight()));
    }
}
