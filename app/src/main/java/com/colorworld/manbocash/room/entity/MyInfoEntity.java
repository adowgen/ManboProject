package com.colorworld.manbocash.room.entity;

import android.content.Context;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Entity;
import androidx.room.InvalidationTracker;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = MyInfoEntity.TABLE_NAME)
public class MyInfoEntity implements Serializable {

    public static final String TABLE_NAME = "myinfo";

    public static final String COLUMN_ID = BaseColumns._ID;

    public static final String COLUMN_EMAIL = "email";

    public static final String COLUMN_NICKNAME = "nickname";

    public static final String COLUMN_PHOTOURL = "photourl";

    public static final String COLUMN_REFERCODE = "refercode";

    public static final String COLUMN_REFEREE = "referee";

    public static final String COLUMN_REGISTERBY = "registerby";

    public static final String COLUMN_UID = "uid";

    public static final String COLUMN_HEIGHT = "height";

    public static final String COLUMN_WEIGHT = "weight";

    public static final String COLUMN_CREATIONTIMESTAMP = "creationTimestamp";

    public static final String COLUMN_LASTSIGNINTIMESTAMP = "lastSignInTimestamp";


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "nickname")
    public String nickname;

    @ColumnInfo(name = "photourl")
    public String photourl;

    @ColumnInfo(name = "refercode")
    public String refercode;

    @ColumnInfo(name = "referee")
    public String referee;

    @ColumnInfo(name = "registerby")
    public String registerby;

    @ColumnInfo(name = "uid")
    public String uid;

    @ColumnInfo(name = "height")
    public String height;

    @ColumnInfo(name = "weight")
    public String weight;

    @ColumnInfo(name = "creationTimestamp")
    public String creationTimestamp;

    @ColumnInfo(name = "lastSignInTimestamp")
    public String lastSignInTimestamp;

}
