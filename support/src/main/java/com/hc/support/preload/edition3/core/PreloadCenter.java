package com.hc.support.preload.edition3.core;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.ViewTreeObserver;

public class PreloadCenter {
    private final Map<ViewPager, PreloadManager> preloadManagerMap = new HashMap<>();
    private final Map<String, Field> mFieldMap = new HashMap<>();
    private static final String ITEMS_FIELD = "mItems";
    private static final String OBJECT_FIELD = "object";
    private static final String POSITION_FIELD = "position";

    private Field getField(Object object, String name) throws NoSuchFieldException {
        Field itemObjectsField = mFieldMap.get(name);
        if (itemObjectsField == null) {
            itemObjectsField = object.getClass().getDeclaredField(name);
            itemObjectsField.setAccessible(true);
            mFieldMap.put(ITEMS_FIELD, itemObjectsField);
        }
        return itemObjectsField;
    }

    public PreloadManager getOrCreate(final ViewPager viewPager) {
        PreloadManager preloadManager = preloadManagerMap.get(viewPager);
        if (preloadManager == null) {
            preloadManager = PreloadManager.newInstance(0.02f, Integer.MAX_VALUE);
            preloadManagerMap.put(viewPager, preloadManager);
            viewPager.getViewTreeObserver().addOnWindowAttachListener(
                    new ViewTreeObserver.OnWindowAttachListener() {
                        @Override
                        public void onWindowAttached() {}

                        @Override
                        public void onWindowDetached() {
                            preloadManagerMap.remove(viewPager);
                        }
                    });
        }
        return preloadManager;
    }

    public void listeningPreloadAction(Fragment fragment, PreloadAction preloadAction) {
        listeningPreloadAction(fragment, preloadAction, null);
    }

    public void listeningPreloadAction(Fragment fragment, PreloadAction preloadAction, PreloadFilter preloadFilter) {
        ViewPager viewPager = findViewPager(fragment.getView());
        PreloadManager preloadManager = getOrCreate(viewPager);
        if (viewPager == null) {
            throw new IllegalArgumentException("fragment must attach to viewPager");
        }
        try {
            Field itemObjectsField = getField(viewPager, ITEMS_FIELD);
            List<Object> itemObjects = (List<Object>) itemObjectsField.get(viewPager);
            if (itemObjects.size() > 0) {
                for (Object itemObject : itemObjects) {
                    Field objectField = getField(itemObject, OBJECT_FIELD);
                    Field positionField = getField(itemObject, POSITION_FIELD);
                    Object sFragment = objectField.get(itemObject);
                    int position = (int) positionField.get(itemObject);
                    if (fragment == sFragment) {
                        preloadManager.register(viewPager, fragment, position, preloadAction, preloadFilter);
                        break;
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static ViewPager findViewPager(View view) {
        View parent = (View) view.getParent();
        if (parent instanceof ViewPager) {
            return (ViewPager) parent;
        }
        return null;
    }

    private static class Instance {
        static PreloadCenter preloadCenter;
        static {
            preloadCenter = new PreloadCenter();
        }
    }

    public static PreloadCenter getInstance() {
        return Instance.preloadCenter;
    }
}