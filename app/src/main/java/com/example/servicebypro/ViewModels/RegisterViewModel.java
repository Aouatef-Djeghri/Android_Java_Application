package com.example.servicebypro.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Repositories.ImagesRepository;
import com.example.servicebypro.Repositories.ServiceRepository;
import com.example.servicebypro.Repositories.UserRepository;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class RegisterViewModel extends AndroidViewModel {


    public RegisterViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "SearchFragment view model constructor");
    }

    private static final String TAG = "SearchViewModel";

    public MutableLiveData<ResponseBody> responseBody;
    private ImagesRepository imagesRepository;
    public MutableLiveData<ErrorResponse> errorResponse;
    private UserRepository userRepository;
    public MutableLiveData<UploadResponse> uploadResponse = new MutableLiveData<>();

    public LiveData<ResponseBody> createUser(User user) {
        userRepository = userRepository.getInstance();
        responseBody = userRepository.createUser(user);
        return responseBody;
    }

    public LiveData<UploadResponse> uploadIdImage(MultipartBody.Part body, String dir,String imageName) {
        imagesRepository = imagesRepository.getInstance();
        uploadResponse = imagesRepository.uploadIdImage(body,dir,imageName);
        return uploadResponse;
    }

    public LiveData<ErrorResponse> getToast() {

        userRepository = userRepository.getInstance();
        errorResponse = userRepository.getToast();
        return errorResponse;
    }

    public LiveData<ErrorResponse> getToastImage() {

        imagesRepository = imagesRepository.getInstance();
        errorResponse = imagesRepository.getToast();
        return errorResponse;
    }

}