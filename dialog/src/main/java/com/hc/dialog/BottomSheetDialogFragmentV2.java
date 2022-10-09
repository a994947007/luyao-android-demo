package com.hc.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.*;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jny.android.demo.base_util.InflaterUtils;

public class BottomSheetDialogFragmentV2 extends BottomSheetDialogFragment {
    private Dialog dialog;
    private View contentView;
    private AppCompatActivity activity;

    public BottomSheetDialogFragmentV2(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.dialog = super.onCreateDialog(savedInstanceState);
        return this.dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = InflaterUtils.inflate(activity, R.layout.bsd_v3_layout);
        dialog.setContentView(contentView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        addLifecycleListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeLifecycleListener();
    }

    /**
     * 这里可以使用DefaultLifecycleObserver代替，不需要使用注解
     */
/*    private final LifecycleObserver mLifecycleObserver = new LifecycleObserver() {
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause(LifecycleOwner owner) {
            dialog.getWindow().setWindowAnimations(0);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume(LifecycleOwner owner) {
            dialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
        }
    };*/

    private final DefaultLifecycleObserver mLifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onPause(@NonNull LifecycleOwner owner) {
            dialog.getWindow().setWindowAnimations(R.style.Animation_Enter_None_BottomSheetDialog);
        }
    };

    private void addLifecycleListener() {
        activity.getLifecycle().addObserver(mLifecycleObserver);
    }

    private void removeLifecycleListener() {
        activity.getLifecycle().removeObserver(mLifecycleObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
