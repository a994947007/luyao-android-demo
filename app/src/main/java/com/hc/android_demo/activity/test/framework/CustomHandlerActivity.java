package com.hc.android_demo.activity.test.framework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.hc.android_demo.R;
import com.hc.my_views.SimpleRecyclerView;
import com.hc.support.looper.MyHandler;
import com.hc.support.looper.MyLooper;
import com.hc.support.looper.MyMessage;
import com.hc.util.ThreadUtils;
import com.hc.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomHandlerActivity extends AppCompatActivity {

    private final List<Pair<String, Runnable>> mItems = new ArrayList<>();
    {
        addItem("测试异步handler，切主线程Toast", this::testMyHandler);
    }

    private MyHandler myHandler;
    private MyLooper looper;

    private void testMyHandler() {
        MyMessage message1 = MyMessage.obtain();
        message1.what = 1;
        myHandler.sendMessage(message1);

        MyMessage message2 = MyMessage.obtain();
        message2.what = 2;
        myHandler.sendMessage(message2);

        MyMessage message3 = MyMessage.obtain();
        message3.what = 3;
        myHandler.sendMessage(message3);

        MyMessage message4 = MyMessage.obtain();
        message4.what = 4;
        myHandler.sendMessage(message4);
    }

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_handler);

        SimpleRecyclerView homeRecyclerView = findViewById(R.id.home_recycler_view);
        homeRecyclerView.setLayoutRes(R.layout.recycler_item);
        homeRecyclerView.setTitleRes(R.id.recycler_item_text_view);
        homeRecyclerView.bind(mItems);

        new Thread(() -> {
            MyLooper.prepare();
            looper = MyLooper.myLooper();
            myHandler = new MyHandler(looper) {
                @Override
                public void handleMessage(MyMessage message) {
                    Log.d("CustomHandlerActivity", Thread.currentThread().getName() + " --> test handler " + message.what);
                    ThreadUtils.runInMainThread(() -> ToastUtils.show("test handler " + message.what, Toast.LENGTH_SHORT));

                    try {
                        Thread.sleep(100);  // 模拟耗时
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            MyLooper.loop();
        }).start();
    }

    private void addItem(String key, Runnable runnable) {
        mItems.add(new Pair<>(key, runnable));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        looper.quit();
    }
}