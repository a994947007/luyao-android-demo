package com.hc.android_demo.fragment.content.framework.bean;

import androidx.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.hc.base.framework.Syncable;
import com.hc.base.framework.SyncableRegister;

@AutoService({SyncableRegister.class})
public class UserSyncableRegister implements SyncableRegister {

    @NonNull
    @Override
    public Class<?> targetClass() {
        return User.class;
    }


    @NonNull
    @Override
    public <T> Syncable<T> create(@NonNull T t) {
        return (Syncable<T>) new UserSyncable((User)t);
    }
}
