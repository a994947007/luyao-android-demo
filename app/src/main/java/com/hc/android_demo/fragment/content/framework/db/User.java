package com.hc.android_demo.fragment.content.framework.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public Integer uid;

    @ColumnInfo(name = "username")
    public String username;
}
