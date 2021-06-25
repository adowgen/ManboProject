package com.colorworld.manbocash.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.colorworld.manbocash.room.entity.MyInfoEntity;

import java.util.List;

@Dao
public interface MyInfoDao {

    @Insert
    public void insertMyInfo(MyInfoEntity myInfo);

    @Update
    public void updateMyInfo(MyInfoEntity myInfo);

    @Delete
    public void deleteMyInfo(MyInfoEntity myInfo);

    @Query("SELECT * FROM " + MyInfoEntity.TABLE_NAME + " WHERE " + MyInfoEntity.COLUMN_UID + " = :uid")
    public MyInfoEntity selectInfoDataByUID(String uid);
//    selectByRegisterBy

//    public List<MyInfoEntity> selectByRegisterBy(boolean registerby);

    //    @Query(" SELECT * FROM " + MySteps.TABLE_NAME)
//    public MyStep[] selectAll();

}
