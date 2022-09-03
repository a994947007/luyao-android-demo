package com.hc.support.skin.cache;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class LuDataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "lu_db";

    public static LuDataBase create(Context context) {
        return Room.databaseBuilder(context, LuDataBase.class, DATABASE_NAME)
                .build();
    }

    public abstract SkinConfigDao skinConfigDao();
}
