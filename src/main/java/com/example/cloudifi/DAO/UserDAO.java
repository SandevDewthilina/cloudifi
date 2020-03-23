package com.example.cloudifi.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cloudifi.entitiies.UserEntity;
import com.example.cloudifi.model.UsersModel;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(List<UserEntity> userEntities);

    @Query("SELECT * FROM user_table")
    LiveData<List<UserEntity>> getUserDetails();
}
