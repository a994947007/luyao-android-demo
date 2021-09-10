package com.hc.android_demo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.hc.android_demo.R;
import com.hc.util.ViewUtils;

public class FullScreenDialog extends Dialog {
    private Activity activity;
    public FullScreenDialog(@NonNull Context context) {
        this(context, 0);
    }

    public FullScreenDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_full_screen);

        getWindow().setDimAmount(0f);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        ViewUtils.getDisplayHeight(activity);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 设置宽度
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }
}
