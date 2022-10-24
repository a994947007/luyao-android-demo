package com.hc.android_demo;

import com.jny.android.demo.api.Res;
import com.jny.android.demo.arouter_annotations.ARouter;

@ARouter(path = "/res/activity_main", group = "res")
public class ResRegister implements Res {
    @Override
    public int getRes() {
        return R.layout.activity_main;
    }
}
