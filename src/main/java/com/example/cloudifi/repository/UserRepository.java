package com.example.cloudifi.repository;

import android.app.Application;
import android.content.Context;

import androidx.room.Query;

import com.example.cloudifi.DAO.UserDAO;
import com.example.cloudifi.database.UserDatabase;
import com.example.cloudifi.entitiies.UserEntity;
import com.example.cloudifi.model.UsersModel;

import java.util.List;

public class UserRepository {

    private UserDAO userDAO;
    private List<UserEntity> usersModelList;

    public UserRepository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        userDAO = db.userDAO();
    }

    public void insertUserList(final List<UserEntity> usersModelList) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insertUser(usersModelList);
        });
    }

}
