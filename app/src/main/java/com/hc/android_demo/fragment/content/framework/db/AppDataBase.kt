package com.hc.android_demo.fragment.content.framework.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jny.android.demo.base_util.AppEnvironment

@Database(entities = [Comment::class],  version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun commentDao():  CommentDao

    companion object {
        private const val DB_NAME = "AppDB.db"

        private val INSTANCE: AppDataBase by lazy {
            Room.databaseBuilder(AppEnvironment.getAppContext(), AppDataBase::class.java, DB_NAME)
                .build()
        }

        @JvmStatic
        fun getInstance() = INSTANCE
    }
}