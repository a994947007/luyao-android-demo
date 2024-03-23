package com.hc.support.singleton;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public final class Singleton {

    private static final Map<Class<?>, Object> beans = new HashMap<>();

    private static final Map<Class<? extends Annotation>, BeanCreator> beanCreators = new HashMap<>();

    static {
        beanCreators.put(com.hc.support.singleton.annotation.Singleton.class, new SingletonBeanCreator());
    }

    public static <T> T get(Class<T> clazz) {
        Object o = beans.get(clazz);
        if (o != null) {
            return (T) o;
        }
        for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
            BeanCreator beanCreator = beanCreators.get(declaredAnnotation.annotationType());
            if (beanCreator != null) {
                T t = (T) beanCreator.create(clazz);
                beans.put(clazz, t);
                return t;
            }
        }
        return null;
    }

    public static void registerBeanCreator(Class<? extends Annotation> annotationClazz, BeanCreator beanCreator) {
        beanCreators.put(annotationClazz, beanCreator);
    }
}
