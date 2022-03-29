package com.hc.bottom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.HalfBottomSheetBehavior;
import com.hc.dialog.R;

public class HalfBottomSheetDialog extends AppCompatDialog {

    protected HalfBottomSheetBehavior<View> mBehavior;
    protected FrameLayout mContainer;

    public HalfBottomSheetDialog(Context context) {
        super(context);
    }

    public HalfBottomSheetDialog(Context context, int theme) {
        super(context, theme);
    }

    protected HalfBottomSheetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    @SuppressLint("ClickableViewAccessibility")
    private View wrapContentView(@LayoutRes int layoutResId, View view, ViewGroup.LayoutParams params) {
        if (mContainer == null) {
            mContainer = (FrameLayout) View.inflate(getContext(), R.layout.half_bottom_sheet_layout, null);
            FrameLayout bottomSheet = (FrameLayout) mContainer.findViewById(R.id.design_bottom_sheet);
            mBehavior = (HalfBottomSheetBehavior)BottomSheetBehavior.from(bottomSheet);
        }
        CoordinatorLayout coordinator = (CoordinatorLayout) mContainer.findViewById(R.id.coordinator);
        if (layoutResId != 0 && view == null) {
            view = getLayoutInflater().inflate(layoutResId, coordinator, false);
        }
        FrameLayout bottomSheet = (FrameLayout) mContainer.findViewById(R.id.design_bottom_sheet);
        bottomSheet.removeAllViews();
        if (params == null) {
            bottomSheet.addView(view);
        } else {
            bottomSheet.addView(view, params);
        }
        coordinator.findViewById(R.id.touch_outside).setOnClickListener(v -> {
            if (isShowing()) {
                cancel();
            }
        });
        bottomSheet.setOnTouchListener((view1, event) -> true);
        return mContainer;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(wrapContentView(0, view, null));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(wrapContentView(layoutResID, null, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(wrapContentView(0, view, params), params);
    }
}
