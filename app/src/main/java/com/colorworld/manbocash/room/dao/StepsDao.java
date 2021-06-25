package com.colorworld.manbocash.room.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.colorworld.manbocash.room.entity.MySteps;

import java.util.ArrayList;

@Dao
public interface StepsDao {
    @Insert
    public void insertCurrentStep(MySteps myStep);

    @Update
    public void updateMyStep(MySteps myStep);

    @Delete
    public void deleteMyStep(MySteps myStep);

    @Query("SELECT * FROM " + MySteps.TABLE_NAME + " WHERE " + MySteps.COLUMN_ISSUCCESS + " = :is_success")
    public MySteps[] selectBySuccess(boolean is_success);

    //    @Query(" SELECT * FROM " + MySteps.TABLE_NAME)
//    public MyStep[] selectAll();

}
