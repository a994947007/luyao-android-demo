package com.jny.android.demo.rxandroid.function;

import java.io.IOException;

/**
 * @param <T> 上游数据
 * @param <R> 下游数据
 */
public interface Function<T, R> {
    R apply(T t) throws IOException;
}
