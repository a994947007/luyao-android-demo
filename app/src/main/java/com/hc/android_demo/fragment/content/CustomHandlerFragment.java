package com.hc.android_demo.fragment.content;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.base.AppEnvironment;
import com.hc.my_views.SimpleRecyclerView;
import com.hc.support.looper.MyHandler;
import com.hc.support.looper.MyLooper;
import com.hc.support.looper.MyMessage;
import com.hc.util.ThreadUtils;
import com.hc.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomHandlerFragment extends Fragment {

    private final List<Pair<String, Runnable>> mItems = new ArrayList<>();
    {
        addItem("测试异步handler，切主线程Toast", this::testMyHandler);
        addItem("自定义HandlerThread/IntentService的使用", this::testMyIntentService);
    }


    private boolean hasBindService = false;

    Intent myServiceIt = new Intent(AppEnvironment.getAppContext(), TestMyIntentService.class);

    private void testMyIntentService() {
        if (hasBindService) {
            return;
        }
        hasBindService = true;
        myServiceIt.putExtra("msg", "这是通过Intent传过来的");
        getActivity().startService(myServiceIt);
    }

    private final ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    /********************************** 自定义Handler、Looper测试 *******************************************/

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_handler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleRecyclerView homeRecyclerView = view.findViewById(R.id.home_recycler_view);
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
    public void onDestroyView() {
        super.onDestroyView();
        looper.quit();
        getActivity().stopService(myServiceIt);
    }
}
