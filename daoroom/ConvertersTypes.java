package code.examples.daoroom;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

import code.examples.interfaces.TypeMeal;

public class ConvertersTypes {

    @TypeConverter
    public static Date dateFromTimestamp(Long longValue){
        return longValue == null ? null : new Date(longValue);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public TypeMeal mealFromInt(Integer intValue){
        int index=0;
        if(intValue!=null)
            index = intValue;
        return TypeMeal.values()[index];
    }

    @TypeConverter
    public Integer mealToInt(TypeMeal typeMeal){
        if(typeMeal==null)
            return 0;
        else
            return typeMeal.ordinal();
    }
}
