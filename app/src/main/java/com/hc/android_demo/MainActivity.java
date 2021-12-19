package com.hc.android_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.hc.android_demo.fragment.CustomViewFragment;
import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.function.Function;
import com.hc.support.rxJava.observable.Observable;
import com.hc.support.rxJava.observable.ObservableSource;
import com.hc.support.rxJava.observer.Consumer;
import com.hc.support.rxJava.observer.Observer;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);     // 由于设置了启动加载bg，因此需要将主题设置回去
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, CustomViewFragment.newInstance());
        fragmentTransaction.commitAllowingStateLoss();

        test();
    }

    protected void createFragmentContainer() {

    }

    private void test() {
        final long start = System.currentTimeMillis();

        Observable.merge(Observable.just(1), Observable.just(2))
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                Log.d("MainActivity", "" + integer);
            }
        });
    }
}