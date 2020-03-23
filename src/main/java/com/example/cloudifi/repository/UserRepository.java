package com.example.cloudifi.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cloudifi.DAO.UserDAO;
import com.example.cloudifi.database.UserDatabase;
import com.example.cloudifi.entitiies.UserEntity;

import java.util.List;

public class UserRepository {

    private UserDAO userDAO;
    private LiveData<List<UserEntity>> usersModelList;

    public UserRepository(Application application) {
        UserDatabase db = UserDatabase.getDatabase(application);
        userDAO = db.userDAO();
        usersModelList = userDAO.getUserDetails();
    }

    public void insertUserList(final List<UserEntity> usersModelList) {
        UserDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insertUser(usersModelList);
        });
    }

    public LiveData<List<UserEntity>> getUsersModelList() {
        return usersModelList;
    }
}
