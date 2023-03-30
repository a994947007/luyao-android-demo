package com.hc.support.mvi;

public class MultiState2<A, B> {

    public A t1;
    public B t2;

    public MultiState2(A t1, B t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public static <A, B> MultiState2<A, B> of(A t1, B t2) {
        return new MultiState2<>(t1, t2);
    }
}
