package com.hc.android_demo.fragment.content.framework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment)

    @Query("SELECT * FROM comments")
    fun getAll(): Flow<List<Comment>>

    @Query("SELECT * FROM comments where text = :text")
    fun findCommentByText(text: String): Flow<Comment>
}