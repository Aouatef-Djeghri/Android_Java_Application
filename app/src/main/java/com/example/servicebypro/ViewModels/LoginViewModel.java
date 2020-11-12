package com.example.servicebypro.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Repositories.ServiceRepository;
import com.example.servicebypro.Repositories.UserRepository;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    public LoginViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "SearchFragment view model constructor");
    }

    private static final String TAG = "SearchViewModel";
    public MutableLiveData<User> user;
    public MutableLiveData<ResponseBody> responseBody;
    public MutableLiveData<ErrorResponse> errorResponse;
    private UserRepository userRepository;


    public LiveData<User> login(String email, String password) {
        userRepository = userRepository.getInstance();
        user = userRepository.login(email,password );
        return user;
    }

    public LiveData<ResponseBody> forgetPassword(String email) {
        userRepository = userRepository.getInstance();
        responseBody = userRepository.forgetPassword(email);
        return responseBody;
    }

    public LiveData<ErrorResponse> getToast() {

        userRepository = userRepository.getInstance();
        errorResponse = userRepository.getToast();
        return errorResponse;
    }



}
