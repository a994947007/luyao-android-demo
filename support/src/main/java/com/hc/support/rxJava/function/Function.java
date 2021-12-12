package com.hc.support.rxJava.function;

/**
 * @param <T> 上游数据
 * @param <R> 下游数据
 */
public interface Function<T, R> {
    R apply(T t);
}
