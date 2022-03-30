package com.hc.bottom;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hc.dialog.R;


public class HalfBottomSheetDialogFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AppCompatDialog(getContext(), getTheme());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogBg;
    }
}
