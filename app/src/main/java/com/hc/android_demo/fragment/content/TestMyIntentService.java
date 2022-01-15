package com.hc.android_demo.fragment.content;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hc.support.looper.MyIntentService;
import com.hc.support.util.TextUtils;
import com.hc.util.ThreadUtils;
import com.hc.util.ToastUtils;

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
