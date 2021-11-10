package com.hc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

public class LandscapeInputFragment extends DialogFragment {
    private Activity activity;
    public EditText editText;
    private View contentView;

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
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        WindowManager m = getDialog().getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(lp);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        editText = contentView.findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AppCompatDialog(activity, R.style.inputDialog);
    }
}
