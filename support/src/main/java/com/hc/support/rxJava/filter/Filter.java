package com.hc.support.rxJava.filter;

public interface Filter<T> {
    boolean accept(T t);
}
