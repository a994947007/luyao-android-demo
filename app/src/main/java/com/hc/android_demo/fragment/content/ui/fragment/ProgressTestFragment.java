package com.hc.android_demo.fragment.content.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.my_views.ProgressView;
import com.hc.my_views.ProgressView2;
import com.hc.my_views.ProgressView3;

public class ProgressTestFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressView progressView1 = view.findViewById(R.id.progressView);
        ValueAnimator valueAnimator1 = ObjectAnimator.ofFloat(0, 4000);
        valueAnimator1.setDuration(2000);
        valueAnimator1.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            progressView1.updateProgress((int) progress);
        });
        valueAnimator1.start();

        ProgressView2 progressView = view.findViewById(R.id.progressView2);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 4000);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            progressView.updateProgress((int) progress);
        });
        valueAnimator.start();

        ProgressView3 progressView3 = view.findViewById(R.id.progressView3);
        progressView3.start(5000);
    }
}
