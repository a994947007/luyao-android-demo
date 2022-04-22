package com.jny.react_native.component.bottomsheet;

import android.content.Context;
import android.view.MotionEvent;
import androidx.annotation.NonNull;

import com.facebook.react.views.view.ReactViewGroup;

public class ReactBottomSheetLayout extends ReactViewGroup {

    public ReactBottomSheetLayout(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
