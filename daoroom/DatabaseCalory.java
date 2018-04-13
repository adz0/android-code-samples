package code.examples.daoroom;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import code.examples.daoroom.dao.DaoPlate;
import code.examples.daoroom.entity.TableFamilies;
import code.examples.daoroom.entity.TableFamilyElements;
import code.examples.daoroom.entity.TableGroup;
import code.examples.daoroom.entity.TableGroupElements;
import code.examples.daoroom.entity.TableIngredients;
import code.examples.daoroom.entity.TableLanguages;
import code.examples.daoroom.entity.TableLocalization;
import code.examples.daoroom.entity.TableNutritionConsist;
import code.examples.daoroom.entity.TablePlate;
import code.examples.daoroom.entity.TablePlateElements;
import code.examples.daoroom.entity.TableRelationWeights;
import code.examples.daoroom.entity.TableVitamin;
import code.examples.daoroom.entity.TableWeight;
import code.examples.daoroom.dao.DaoFamily;
import code.examples.daoroom.dao.DaoGroup;
import code.examples.daoroom.dao.DaoIngredient;
import code.examples.daoroom.dao.DaoRelationWeights;
import code.examples.daoroom.dao.DaoVitamineral;
import code.examples.daoroom.dao.DaoWeight;
import code.examples.elements.PlateElemImpl;
import code.examples.elements.PlateImpl;

@Database(entities = {
          TableFamilies.class
        , TableFamilyElements.class
        , TableGroup.class
        , TableGroupElements.class
        , TableIngredients.class
        , TableLanguages.class
        , TableLocalization.class
        , TableNutritionConsist.class
        , TableRelationWeights.class
        , TableVitamin.class
        , TableWeight.class
        , TablePlate.class
        , TablePlateElements.class
}
, version = DatabaseCalory.DB_VERSION)
@TypeConverters({ConvertersTypes.class})
public abstract class DatabaseCalory extends RoomDatabase {

    //region Instance database
    private static DatabaseCalory Instance;

    public static String DB_NAME="counterbase.sqlite";
    public static String DB_PATH;
    public static final int DB_VERSION=1;


    public static void checkDBPath(Context context){
		
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH =  context.getFilesDir().getPath() + "/databases/";

        File dbFile = new File(DB_PATH);
        if(!dbFile.exists()){
            try {
                copyDBFile(context);
            }catch (IOException e){
                Log.e("COPY DB", e.getMessage());
            }
        }
    }
	
    private static void copyDBFile(Context context) throws IOException {
        InputStream mInput = context.getAssets().open(DB_NAME);

        File dirDatabase = new File(DB_PATH);
        if( !dirDatabase.exists()){
            if(!dirDatabase.mkdirs())
                throw new IOException("create database dir error");
        }

        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    public static DatabaseCalory getInstance(Context context){
        if(Instance==null){
            synchronized (DatabaseCalory.class){
                checkDBPath(context);
                Instance = Room
                        .databaseBuilder(context, DatabaseCalory.class, DB_PATH+DB_NAME)
                        .addMigrations(new Migration(DB_VERSION, DB_VERSION+1) {
                            @Override
                            public void migrate(@NonNull SupportSQLiteDatabase database) {

                               if(DB_VERSION==2){
                                   database.beginTransaction();
                                   database.execSQL("CREATE TABLE plates (\n" +
                                           "    id         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                                           "                       NOT NULL,\n" +
                                           "    plate_date INTEGER,\n" +
                                           "    meal_type  INTEGER NOT NULL \n" +
                                           ");");
                                   database.execSQL("CREATE TABLE plate_elements (\n" +
                                           "    id             INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                                           "                           NOT NULL,\n" +
                                           "    id_plate       INTEGER ,\n" +
                                           "    id_group       INTEGER ,\n" +
                                           "    id_family      INTEGER ,\n" +
                                           "    id_ingredient  INTEGER ,\n" +
                                           "    id_weight_alt  INTEGER ,\n" +
                                           "    weight_in_base REAL    DEFAULT (0) \n" +
                                           ");");
                                   database.endTransaction();
                               }
                            }
                        })
                        .build();
            }
        }
        return Instance;
    }
    //endregion Instance database


    abstract public DaoGroup daoGroup();
    abstract public DaoIngredient daoIngredient();
    abstract public DaoFamily daoFamily();
    abstract public DaoPlate daoPlate();
}
