package com.luyao.android.demo.plugin_module_loader;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Objects;

import dalvik.system.DexClassLoader;

public class PluginModuleLoader {

    private static class Instance {
        private final static PluginModuleLoader pluginModuleLoader = new PluginModuleLoader();
    }

    public static PluginModuleLoader getInstance() {
        return Instance.pluginModuleLoader;
    }

    public void load(Context context, String modulePath, Callback callback) {
        try {
            Class<?> baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = baseDexClassLoaderClass.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);

            // 宿主的类加载器
            ClassLoader pathClassLoader = context.getClassLoader();
            Object hostPathList = pathListField.get(pathClassLoader);
            Object [] hostDexElements = (Object[]) dexElementsField.get(hostPathList);

            // 插件的类加载器
            DexClassLoader dexClassLoader = new DexClassLoader(
                    modulePath,
                    context.getCacheDir().getAbsolutePath(),
                    null,
                    pathClassLoader
            );
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object [] pluginDexElements = (Object[]) dexElementsField.get(pluginPathList);

            if (hostDexElements != null && pluginDexElements != null) {
                Object[] newElements = (Object[]) Array.newInstance(Objects.requireNonNull(hostDexElements.getClass().getComponentType()),
                        hostDexElements.length + pluginDexElements.length);
                System.arraycopy(hostDexElements, 0, newElements,
                        0, hostDexElements.length);
                System.arraycopy(pluginDexElements, 0, newElements,
                        hostDexElements.length, pluginDexElements.length);
                dexElementsField.set(hostPathList, newElements);
            }
            if (callback != null) {
                callback.onSuccess();
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError();
            }
        }
    }
}
