package com.example.cloudifi.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cloudifi.entitiies.UserEntity;
import com.example.cloudifi.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private LiveData<List<UserEntity>> userDetailLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        UserRepository userRepository = new UserRepository(application);
        userDetailLiveData = userRepository.getUsersModelList();
    }

    public LiveData<List<UserEntity>> getUserDetailLiveData() {
        return userDetailLiveData;
    }
}
