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
import com.example.servicebypro.Remote.Models.UploadResponse;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.Repositories.ImagesRepository;
import com.example.servicebypro.Repositories.ServiceRepository;
import com.example.servicebypro.Repositories.UserRepository;
import com.example.servicebypro.Repositories.WorkRepository;


import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class AddFragmentViewModel extends AndroidViewModel {

    public AddFragmentViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "SearchFragment view model constructor");
    }

    private static final String TAG = "SearchViewModel";
    public MutableLiveData<ArrayList<Categorie>> mCategoryList;
    public MutableLiveData<ArrayList<Service>> mServicesList;
    public MutableLiveData<ArrayList<User>> mUsersList;
    public MutableLiveData<ErrorResponse> errorResponse;
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;
    private WorkRepository workRepository;
    private ImagesRepository imagesRepository;
    public MutableLiveData<ResponseBody> responseBody = new MutableLiveData<>();
    public MutableLiveData<UploadResponse> uploadResponse = new MutableLiveData<>();

    public LiveData<ArrayList<Categorie>> getCategoryList() {
        serviceRepository = serviceRepository.getInstance();
        mCategoryList = serviceRepository.getCategoriesList();
        return mCategoryList;
    }

    public LiveData<ArrayList<Service>> getCategorieServicesList(int idCategory) {
        serviceRepository = serviceRepository.getInstance();
        mServicesList = serviceRepository.getCategorieServicesList(idCategory);
        return mServicesList;
    }

    public LiveData<ResponseBody> createWorkPost(Work work) {
        workRepository = workRepository.getInstance();
        responseBody = workRepository.createWorkPost(work);
        return responseBody;
    }

    public LiveData<ResponseBody> createWorkProposal(com.example.servicebypro.Remote.Models.Application application) {
        workRepository = workRepository.getInstance();
        responseBody = workRepository.createWorkProposal(application);
        return responseBody;
    }

    public LiveData<ArrayList<User>> listAllUsersByService(int serviceId, int clientId) {
        userRepository = userRepository.getInstance();
        mUsersList = userRepository.listAllUsersByService(serviceId, clientId);
        return mUsersList;
    }


    public LiveData<ErrorResponse> getToastService() {

        serviceRepository = serviceRepository.getInstance();
        errorResponse = serviceRepository.getToast();
        return errorResponse;
    }

    public LiveData<UploadResponse> uploadWorkImages(MultipartBody.Part imageOne,MultipartBody.Part imageTwo,MultipartBody.Part imageTHree) {
        imagesRepository = imagesRepository.getInstance();
        uploadResponse = imagesRepository.uploadWorkImages(imageOne,imageTwo,imageTHree);
        return uploadResponse;
    }



    public LiveData<ErrorResponse> getToastWork() {

        workRepository = workRepository.getInstance();
        errorResponse = workRepository.getToastWork();
        return errorResponse;
    }

    public LiveData<ErrorResponse> getToast() {

        imagesRepository = imagesRepository.getInstance();
        errorResponse = imagesRepository.getToast();
        return errorResponse;
    }
}
