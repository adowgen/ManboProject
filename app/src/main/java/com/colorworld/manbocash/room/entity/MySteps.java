package com.colorworld.manbocash.room.entity;


import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = MySteps.TABLE_NAME)
public class MySteps {
    public static final String TABLE_NAME = "mysteps";

    public static final String COLUMN_ID = BaseColumns._ID;

    public static final String COLUMN_STEP = "step";

    public static final String COLUMN_DATE = "date";

    public static final String COLUMN_ISSUCCESS = "is_success";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @ColumnInfo(name = "step")
    public int step;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "is_success")
    public boolean isSuccess;


}
