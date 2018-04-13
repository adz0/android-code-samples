package code.examples.daoroom.entity;

import android.arch.persistence.room.ColumnInfo;


public class GroupsFamilyElements {
    @ColumnInfo(name = "id_group")
    public int idGroup;
    @ColumnInfo(name = "id_family")
    public int idFamily;
}
