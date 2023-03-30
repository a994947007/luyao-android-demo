package com.hc.support.mvi;

public class MultiState3<A, B, C> {

    public A t1;
    public B t2;
    public C t3;

    public MultiState3(A t1, B t2, C t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public static <A, B, C> MultiState3<A, B, C> of(A t1, B t2, C t3) {
        return new MultiState3<>(t1, t2, t3);
    }
}
