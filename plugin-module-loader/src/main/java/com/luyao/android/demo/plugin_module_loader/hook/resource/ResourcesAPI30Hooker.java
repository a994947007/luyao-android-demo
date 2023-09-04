package com.luyao.android.demo.plugin_module_loader.hook.resource;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.luyao.android.demo.plugin_module_loader.PluginConfig;
import com.luyao.android.demo.plugin_module_loader.PluginModulePathMapper;
import com.luyao.android.demo.plugin_module_loader.hook.AMSHookerManager;
import com.luyao.android.demo.plugin_module_loader.hook.AbstractAMSHooker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResourcesAPI30Hooker extends AbstractAMSHooker {
    public ResourcesAPI30Hooker(Chain chain) {
        super(chain);
    }

    @Override
    protected boolean doHook() {

        Class<?> activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            Field mSCurrentActivityThread = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            mSCurrentActivityThread.setAccessible(true);
            mInstrumentationField.setAccessible(true);

            Object activityThread = mSCurrentActivityThread.get(null);
            Object mInstrumentation = mInstrumentationField.get(activityThread);
            InstrumentationProxy instrumentationProxy = new InstrumentationProxy((Instrumentation) mInstrumentation);
            mInstrumentationField.set(activityThread, instrumentationProxy);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static class InstrumentationProxy extends Instrumentation {

        private final Instrumentation proxy;

        public InstrumentationProxy(Instrumentation instrumentation) {
            this.proxy = instrumentation;
        }

        @Override
        public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            Activity activity = proxy.newActivity(cl, className, intent);
            hookResource(activity);
            return activity;
        }

        @Override
        public Activity newActivity(Class<?> clazz,
                                    Context context,
                                    IBinder token,
                                    Application application,
                                    Intent intent,
                                    ActivityInfo info,
                                    CharSequence title,
                                    Activity parent,
                                    String id,
                                    Object lastNonConfigurationInstance
        ) throws IllegalAccessException, InstantiationException {
            Activity activity = proxy.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
            hookResource(activity);
            return activity;
        }

        private void hookResource(Activity activity) {
            try {
                Field mResourcesField = ContextThemeWrapper.class.getDeclaredField("mResources");
                mResourcesField.setAccessible(true);
                Resources resources = loadResources(activity.getClass().getName());
                if (resources != null) {
                    mResourcesField.set(activity, resources);
                }
                Log.d("Abc", "bc");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        private Resources loadResources(String className) {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath", String.class);
                addAssetPathMethod.setAccessible(true);
                PluginConfig pluginConfig = PluginModulePathMapper.getPluginConfig(className);
                if (pluginConfig != null) {
                    addAssetPathMethod.invoke(assetManager, pluginConfig.modulePath);
                    Resources resources = AMSHookerManager.getInstance().getContext().getResources();
                    return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
