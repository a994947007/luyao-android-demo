package com.hc.support.skin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.support.skin.util.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {

    private static final Class<?> [] mConstructorSignature = new Class[] { Context.class, AttributeSet.class };

    private static final HashMap<String, Constructor<? extends View>> mConstructorMap = new HashMap<>();

    private static final List<String> mClassPrefixList = new ArrayList<>();

    private final SkinAttribute skinAttribute;

    private final Activity activity;

    static {
        mClassPrefixList.add("android.widget.");
        mClassPrefixList.add("android.webkit.");
        mClassPrefixList.add("android.app.");
        mClassPrefixList.add("android.view.");
    }

    public SkinLayoutInflaterFactory(Activity activity) {
        this.activity = activity;
        this.skinAttribute = new SkinAttribute();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = createSDKView(name, context, attrs);    // android.
        if (null == view) {
            view = createView(name, context, attrs);
        }
        if (null != view) {
            skinAttribute.look(view, attrs);
        }
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    private View createSDKView(String name, Context context, AttributeSet attributeSet) {
        if (-1 != name.indexOf('.')) {
            return null;
        }
        for (String prefix : mClassPrefixList) {
            View view = createView(prefix + name, context, attributeSet);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    private View createView(String name, Context context, AttributeSet attributeSet) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attributeSet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SkinThemeUtils.updateStatusBarColor(activity);
        }
        skinAttribute.applySkin();
    }
}
