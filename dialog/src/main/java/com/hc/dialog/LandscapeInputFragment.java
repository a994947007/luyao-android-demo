package com.hc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import com.hc.util.SystemServiceUtils;

public class LandscapeInputFragment extends DialogFragment {
    private Activity activity;
    public CustomEditText editText;
    private View contentView;
    private boolean isExpanded;

    public LandscapeInputFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.landscape_input_panel, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        editText = contentView.findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // 两种情况只能触发一个
        //父Activity布局未变动，LandscapeInputFragment布局变化
        editText.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> collapseDismiss());
        // 父Activity布局变动，导致LandscapeInputFragment布局未变化
        activity.getWindow().getDecorView().addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> collapseDismiss());
    }

    private void collapseDismiss() {
        int keyboardHeight = SystemServiceUtils.getKeyboardHeight(activity);
        if (keyboardHeight == 0 && isExpanded) {
            isExpanded = false;
            dismissAllowingStateLoss();
        } else if (keyboardHeight > 0) {
            isExpanded = true;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AppCompatDialog(activity, R.style.inputDialog);
    }
}
