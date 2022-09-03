package com.hc.support.skin.cache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SkinConfigDao {
    @Insert
    void insert(SkinConfig skinConfig);

    @Query("SELECT * FROM T_SKIN_CONFIG WHERE M_SKIN_KEY = :skinKey")
    SkinConfig getSkinConfigByKey(String skinKey);

    @Query("DELETE FROM T_SKIN_CONFIG WHERE M_SKIN_KEY = :skinKey")
    void deleteSkinConfig(String skinKey);
}
