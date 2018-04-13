package code.examples.daoroom.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "families")
public class TableFamilies {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "name_key")
    @NonNull
    public String nameKey;
    public int type;
    public int member;
    public String icon;
}
