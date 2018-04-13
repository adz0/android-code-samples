package code.examples.daoroom.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "family_elements")
public class TableFamilyElements {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "parent")
    @NonNull
    public int idFamily;
    @ColumnInfo(name = "elem_id")
    @NonNull
    public int idElem;
}
