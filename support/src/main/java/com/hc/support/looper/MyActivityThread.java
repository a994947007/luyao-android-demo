package com.hc.support.looper;

/**
 * 模仿Android内部Looper和Handler
 */
public class MyActivityThread {

    // 模拟main函数
    public static void main() {
        MyLooper.prepare();
        MyLooper.loop();
    }
}
