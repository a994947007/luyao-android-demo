package com.hc.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

public class LuActivity extends BaseActivity{

    private void createTimeLogger() {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new LayoutInflater.Factory2() {
            @Nullable
            @Override
            public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                long startTime = System.currentTimeMillis();
                View view = getDelegate().createView(parent, name, context, attrs);
                Log.d("ViewCreateTimes", "name:" + name + ",times:" + (System.currentTimeMillis() - startTime));
                return view;
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                return null;
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        createTimeLogger();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
