package com.hc.android_demo.fragment.content.framework.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao userDao();

    //数据库名字
    private static final String DB_NAME = "UserDB.db";
    private static volatile UserDB instance;

    public static synchronized UserDB getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UserDB create(final Context context) {
        return Room.databaseBuilder(
                context,
                UserDB.class,
                DB_NAME).build();
    }
}
