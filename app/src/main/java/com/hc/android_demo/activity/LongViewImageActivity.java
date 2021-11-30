package com.hc.android_demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_demo.R;
import com.hc.util.UriUtils;

public class LongViewImageActivity extends AppCompatActivity {
    private SimpleDraweeView draweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_view_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        bindView();
    }

    private void bindView() {
        draweeView = findViewById(R.id.imgView);
        draweeView.setImageURI(UriUtils.getUri(R.drawable.long_image));
        draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_X);
    }
}