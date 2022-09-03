package com.hc.support.skin.cache;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "T_SKIN_CONFIG")
public class SkinConfig {
    @PrimaryKey(autoGenerate = true)
    public long mId;
    @ColumnInfo(name = "M_SKIN_KEY")
    public String mSkinKey;
    @ColumnInfo(name = "M_SKIN_PATH")
    public String mSkinPath;

    @Keep
    public SkinConfig(String skinKey, String skinPath) {
        this.mSkinKey = skinKey;
        this.mSkinPath = skinPath;
    }

    @Keep
    public SkinConfig() { }
}
