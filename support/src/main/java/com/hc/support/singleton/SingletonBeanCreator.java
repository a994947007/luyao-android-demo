package com.hc.support.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SingletonBeanCreator implements BeanCreator{

    @Override
    public Object create(Class<?> clazz) {
        if (clazz.isAnnotationPresent(com.hc.support.singleton.annotation.Singleton.class)) {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
