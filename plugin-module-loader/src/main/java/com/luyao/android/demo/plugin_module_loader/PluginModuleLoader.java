package com.luyao.android.demo.plugin_module_loader;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dalvik.system.DexClassLoader;

public class PluginModuleLoader {

    private static class Instance {
        private final static PluginModuleLoader pluginModuleLoader = new PluginModuleLoader();
    }

    public static PluginModuleLoader getInstance() {
        return Instance.pluginModuleLoader;
    }

    private final Map<String, Field> fieldCache = new HashMap<>();
    private static final String PATH_LIST_FIELD = "PATH_LIST_FIELD";
    private static final String DEX_ELEMENTS_FIELD = "DEX_ELEMENTS_FIELD";
    private Object hostPathListCache = null;
    private Object [] hostDexElementsCache = null;

    public void load(Context context, String modulePath, Callback callback) {
        try {
            if (fieldCache.get(PATH_LIST_FIELD) == null) {
                Class<?> baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
                Field pathListField = baseDexClassLoaderClass.getDeclaredField("pathList");
                pathListField.setAccessible(true);
                fieldCache.put(PATH_LIST_FIELD, pathListField);
            }

            if (fieldCache.get(DEX_ELEMENTS_FIELD) == null) {
                Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
                Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                fieldCache.put(DEX_ELEMENTS_FIELD, dexElementsField);
            }

            Field pathListField = fieldCache.get(PATH_LIST_FIELD);
            Field dexElementsField = fieldCache.get(DEX_ELEMENTS_FIELD);

            // 宿主的类加载器
            ClassLoader pathClassLoader = context.getClassLoader();
            if (hostDexElementsCache == null) {
                hostPathListCache = pathListField.get(pathClassLoader);
                hostDexElementsCache = (Object[]) dexElementsField.get(hostPathListCache);
            }

            // 插件的类加载器
            DexClassLoader dexClassLoader = new DexClassLoader(
                    modulePath,
                    context.getCacheDir().getAbsolutePath(),
                    null,
                    pathClassLoader
            );
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object [] pluginDexElements = (Object[]) dexElementsField.get(pluginPathList);

            if (hostDexElementsCache != null && pluginDexElements != null) {
                Object[] newElements = (Object[]) Array.newInstance(Objects.requireNonNull(hostDexElementsCache.getClass().getComponentType()),
                        hostDexElementsCache.length + pluginDexElements.length);
                System.arraycopy(hostDexElementsCache, 0, newElements,
                        0, hostDexElementsCache.length);
                System.arraycopy(pluginDexElements, 0, newElements,
                        hostDexElementsCache.length, pluginDexElements.length);
                dexElementsField.set(hostPathListCache, newElements);
            }
            if (callback != null) {
                callback.onSuccess();
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e);
            }
        }
    }
}
