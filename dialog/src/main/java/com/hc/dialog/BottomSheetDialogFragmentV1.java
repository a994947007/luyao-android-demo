package com.hc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hc.util.InflaterUtils;

public class BottomSheetDialogFragmentV1 extends BottomSheetDialogFragment {
    private Dialog dialog;
    private View contentView;
    private Activity activity;

    public BottomSheetDialogFragmentV1(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.dialog = super.onCreateDialog(savedInstanceState);
        Log.e("BottomSheetDialogFragmentV1", "onCreateDialog");
        return this.dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogBg);      // 用于设置圆角背景透明且无蒙层
        Log.e("BottomSheetDialogFragmentV1", "onCreate");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("BottomSheetDialogFragmentV1", "onCreateView");
        contentView = InflaterUtils.inflate(activity, R.layout.bsd_v3_layout);
        dialog.setContentView(contentView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("BottomSheetDialogFragmentV1", "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("BottomSheetDialogFragmentV1", "onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("BottomSheetDialogFragmentV1", "onDestroyView");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("BottomSheetDialogFragmentV1", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("BottomSheetDialogFragmentV1", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("BottomSheetDialogFragmentV1", "onDestroy");
    }

}
