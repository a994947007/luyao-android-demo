package com.luyao.android.demo.plugin_module_loader.hook.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;

import com.luyao.android.demo.plugin_module_loader.PluginModulePathMapper;
import com.luyao.android.demo.plugin_module_loader.hook.AMSHookerManager;
import com.luyao.android.demo.plugin_module_loader.hook.AbstractAMSHooker;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartActivityAPI30Hooker extends AbstractAMSHooker {

    private static final String ACTUAL_INTENT_ARG = "ACTUAL_INTENT_ARG";
    private static final String HOOK_METHOD = "startActivity";

    private static final String ACTIVITY_CALLBACK_FIELD = "ACTIVITY_CALLBACK_FIELD";
    private static final String INTENT_FIELD = "INTENT_FIELD";

    private static final String LAUNCH_ACTIVITY_ITEM = "android.app.servertransaction.LaunchActivityItem";

    private final Map<String, Field> fieldCache = new HashMap<>();

    public StartActivityAPI30Hooker(Chain chain) {
        super(chain);
    }

    private void hookStart() {
        try {
            Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
            Field iActivityTaskManagerSingletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
            iActivityTaskManagerSingletonField.setAccessible(true);

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field instanceField = singletonClass.getDeclaredField("mInstance");
            instanceField.setAccessible(true);

            Class<?> iActivityTaskManager = Class.forName("android.app.IActivityTaskManager");

            Object iActivityTaskManagerSingleton = iActivityTaskManagerSingletonField.get(null);
            Object activitySingleton = instanceField.get(iActivityTaskManagerSingleton);
            Object activitySingletonProxy = Proxy.newProxyInstance(Thread.currentThread().getClass().getClassLoader(),
                    new Class[]{iActivityTaskManager},
                    (proxy, method, args) -> {
                        if (HOOK_METHOD.equals(method.getName()) && args != null && args.length != 0) {
                            int index = -1;
                            for (int i = 0; i < args.length; i++) {
                                if (args[i] instanceof Intent) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1) {
                                Intent intent = (Intent) args[index];
                                String className = intent.getComponent().getClassName();
                                if (PluginModulePathMapper.contains(className)) {
                                    Intent proxyIntent = new Intent();
                                    proxyIntent.setClassName(AMSHookerManager.getInstance().getContext(), ProxyActivity.class.getName());
                                    proxyIntent.putExtra(ACTUAL_INTENT_ARG, intent);
                                    args[index] = proxyIntent;
                                }
                            }
                        }
                        return method.invoke(activitySingleton, args);
                    });
            instanceField.setAccessible(true);
            instanceField.set(iActivityTaskManagerSingleton, activitySingletonProxy);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void hookCallback() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field mHField = activityThreadClass.getDeclaredField("mH");
            Field mSCurrentActivityThread = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            mSCurrentActivityThread.setAccessible(true);
            mHField.setAccessible(true);

            Object activityThread = mSCurrentActivityThread.get(null);
            Handler handler = (Handler) mHField.get(activityThread);
            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);

            Handler.Callback actualCallback = (Handler.Callback) callbackField.get(handler);
            Handler.Callback proxyCallback = msg -> {
                if (msg.what == 159) {
                    Object item = msg.obj;
                    try {
                        if (fieldCache.get(ACTIVITY_CALLBACK_FIELD) == null) {
                            Class<?> clientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                            Field activityCallbacksField = clientTransactionClass.getDeclaredField("mActivityCallbacks");
                            activityCallbacksField.setAccessible(true);
                            fieldCache.put(ACTIVITY_CALLBACK_FIELD, activityCallbacksField);
                        }
                        Field activityCallbacksField = fieldCache.get(ACTIVITY_CALLBACK_FIELD);
                        List<Object> callbacks = (List<Object>) activityCallbacksField.get(item);
                        if (callbacks != null) {
                            for (Object callback : callbacks) {
                                if (callback.getClass().getName().equals(LAUNCH_ACTIVITY_ITEM)) {
                                    try {
                                        if (fieldCache.get(INTENT_FIELD) == null) {
                                            Field intentField = callback.getClass().getDeclaredField("mIntent");
                                            intentField.setAccessible(true);
                                            fieldCache.put(INTENT_FIELD, intentField);
                                        }
                                        Field intentField = fieldCache.get(INTENT_FIELD);
                                        Intent proxyIntent = (Intent) intentField.get(callback);
                                        Intent actualIntent = proxyIntent.getParcelableExtra(ACTUAL_INTENT_ARG);
                                        if (actualIntent != null) {
                                            intentField.set(callback, actualIntent);
                                        }
                                    } catch (NoSuchFieldException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (actualCallback != null) {
                    return actualCallback.handleMessage(msg);
                }
                return false;
            };
            callbackField.set(handler, proxyCallback);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void hookPackageManager() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(null);
            Class<?> iPackageManagerClassName = Class.forName("android.content.pm.IPackageManager");
            Object proxyPackageManager = Proxy.newProxyInstance(Thread.currentThread().getClass().getClassLoader(),
                    new Class[]{iPackageManagerClassName},
                    (proxy, method, args) -> {
                        if ("getActivityInfo".equals(method.getName())) {
                            int index = -1;
                            for (int i = 0; i < args.length; i++) {
                                if (args[i] instanceof ComponentName) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1) {
                                ComponentName componentName = new ComponentName(AMSHookerManager.getInstance().getContext().getPackageName(),
                                        ProxyActivity.class.getName());
                                args[index] = componentName;
                            }
                        }
                        return method.invoke(sPackageManager, args);
                    });
            sPackageManagerField.set(null, proxyPackageManager);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean doHook() {
        hookStart();
        hookCallback();
        hookPackageManager();
        return true;
    }
}
