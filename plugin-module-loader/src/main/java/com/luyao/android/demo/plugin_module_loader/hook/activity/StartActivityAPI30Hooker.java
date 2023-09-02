package com.luyao.android.demo.plugin_module_loader.hook.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;

import com.luyao.android.demo.plugin_module_loader.hook.AMSHookerManager;
import com.luyao.android.demo.plugin_module_loader.hook.AbstractAMSHooker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class StartActivityAPI30Hooker extends AbstractAMSHooker {

    private static final String ACTUAL_INTENT_ARG = "ACTUAL_INTENT_ARG";

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
                        if ("startActivity".equals(method.getName()) && args != null && args.length != 0) {
                            int i = 0;
                            for (; i < args.length; i++) {
                                if (args[i] instanceof Intent) {
                                    break;
                                }
                            }
                            Intent intent = (Intent) args[i];
                            String className = intent.getComponent().getClassName();
                            if ("com.luyao.android.demo.plugin_module.PluginTestActivity".equals(className)) {
                                Intent proxyIntent = new Intent();
                                proxyIntent.setClassName(AMSHookerManager.getInstance().getContext(), ProxyActivity.class.getName());
                                proxyIntent.putExtra(ACTUAL_INTENT_ARG, intent);
                                args[i] = proxyIntent;
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
                        Class<?> clientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                        Field activityCallbacksField = clientTransactionClass.getDeclaredField("mActivityCallbacks");
                        activityCallbacksField.setAccessible(true);
                        List<Object> callbacks = (List<Object>) activityCallbacksField.get(item);
                        if (callbacks != null) {
                            for (Object callback : callbacks) {
                                if (callback.getClass().getName().equals("android.app.servertransaction.LaunchActivityItem")) {
                                    try {
                                        Field intentField = callback.getClass().getDeclaredField("mIntent");
                                        intentField.setAccessible(true);
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
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
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
                            int i = 0;
                            for (; i < args.length; i++) {
                                if (args[i] instanceof ComponentName) {
                                    break;
                                }
                            }
                            ComponentName componentName = new ComponentName(AMSHookerManager.getInstance().getContext().getPackageName(),
                                    ProxyActivity.class.getName());
                            args[i] = componentName;
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
