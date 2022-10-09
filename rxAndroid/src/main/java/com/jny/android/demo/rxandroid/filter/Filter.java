package com.jny.android.demo.rxandroid.filter;

public interface Filter<T> {
    boolean accept(T t);
}
