package com.jny.android.demo.rxandroid.schedule;

public class Schedules {
    public static Scheduler COMPUTATION = new ComputationScheduler();
    public static Scheduler IO = new IoScheduler();
    public static Scheduler NEW_THREAD = new NewThreadScheduler();
    public static Scheduler MAIN = new MainScheduler();
}
