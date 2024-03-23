package com.hc.android_demo.fragment.content.framework.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val cid: Int,
    @ColumnInfo(name = "text") val text: String
)
