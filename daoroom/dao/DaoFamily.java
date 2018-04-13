package code.examples.daoroom.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import code.examples.elements.InfoFamily;

@Dao
public interface DaoFamily {
    @Query(" SELECT ge.id_group, fa.* FROM group_elements as ge"
            + " INNER JOIN families as fa ON fa.id=ge.id_family")
    List<InfoFamily> getFamilies();
}
