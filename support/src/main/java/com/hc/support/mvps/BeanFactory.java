package com.hc.support.mvps;

public interface BeanFactory {
    <T> BeanWrapper<T> create();
}
