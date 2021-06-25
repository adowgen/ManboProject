package com.colorworld.manbocash.room;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.colorworld.manbocash.room.dao.StepsDao;
import com.colorworld.manbocash.room.entity.MySteps;

@Database(entities = {MySteps.class}, version = 1)
public abstract class StepsDatabase extends RoomDatabase {

    @SuppressWarnings("WeakerAccess")
    public abstract StepsDao getMyStepsDao();

    private static StepsDatabase sInstance;

    public static synchronized StepsDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), StepsDatabase.class, "my_step.db").build();
        }
        return sInstance;
    }


    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `productsFts` USING FTS4("
//                    + "`name` TEXT, `description` TEXT, content=`products`)");


        }
    };


}
