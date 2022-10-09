package com.hc.android_demo.fragment.content.framework.service;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hc.support.looper.MyIntentService;
import com.hc.util.ToastUtils;
import com.jny.android.demo.base_util.TextUtils;
import com.jny.android.demo.base_util.ThreadUtils;

import java.util.Objects;

public class TestMyIntentService extends MyIntentService {
    @Override
    protected void onHandlerIntent(@Nullable Intent intent) {
        String msg = Objects.requireNonNull(intent).getStringExtra("msg");
        if (!TextUtils.isEmpty(msg)) {
            ThreadUtils.runInMainThread(() -> {
                ToastUtils.show(msg, Toast.LENGTH_SHORT);
            });
        }
    }
}
