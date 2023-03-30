package com.hc.support.mvi;

public class MultiState1<T> {

    public T t1;

    public MultiState1(T t1) {
        this.t1 = t1;
    }

    public static <T> MultiState1<T> of(T t) {
        return new MultiState1<>(t);
    }
}
