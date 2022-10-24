package com.jny.android.demo;

public final class ClassUtils {

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object newInstance(RouterBean routerBean) {
        return newInstance(routerBean.getMyClass());
    }
}
