package com.hc.android_demo.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.hc.android_demo.R;
import com.hc.my_views.ProgressView;
import com.hc.my_views.ProgressView2;
import com.hc.my_views.ProgressView3;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ProgressView progressView1 = findViewById(R.id.progressView);
        ValueAnimator valueAnimator1 = ObjectAnimator.ofFloat(0, 4000);
        valueAnimator1.setDuration(2000);
        valueAnimator1.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            progressView1.updateProgress((int) progress);
        });
        valueAnimator1.start();

        ProgressView2 progressView = findViewById(R.id.progressView2);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 4000);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            progressView.updateProgress((int) progress);
        });
        valueAnimator.start();

        ProgressView3 progressView3 = findViewById(R.id.progressView3);
        progressView3.start(5000);
    }
}