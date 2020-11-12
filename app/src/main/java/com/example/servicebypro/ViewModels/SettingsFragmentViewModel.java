package com.example.servicebypro.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Section;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.Repositories.ServiceRepository;
import com.example.servicebypro.Repositories.UserRepository;
import com.example.servicebypro.Repositories.WorkRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class SettingsFragmentViewModel extends AndroidViewModel {

    private static final String TAG = "SearchViewModel";
    public MutableLiveData<ArrayList<Section>> mServiceArtisanList;
    public MutableLiveData<ArrayList<Service>> mServiceArtisanListNoSection;
    public MutableLiveData<ArrayList<Service>> mAllServicesList;
    public MutableLiveData<ResponseBody> responseBody;
    public MutableLiveData<ErrorResponse> errorResponse;
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;

    public SettingsFragmentViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "Work view model constructor");
    }

    public LiveData<ArrayList<Section>> getServicesArtisanList(int idUser) {
        serviceRepository = ServiceRepository.getInstance();
        mServiceArtisanList = serviceRepository.getServicesArtisanList(idUser);
        return mServiceArtisanList;
    }

    public LiveData<ArrayList<Service>> getServicesArtisanListNoSections(int idUser) {
        serviceRepository = ServiceRepository.getInstance();
        mServiceArtisanListNoSection = serviceRepository.getServicesArtisanListNoSections(idUser);
        return mServiceArtisanListNoSection;
    }



    public LiveData<ArrayList<Service>> getAllServicesList() {
        serviceRepository = ServiceRepository.getInstance();
        mAllServicesList = serviceRepository.getAllServicesList();
        return mAllServicesList;
    }

    public LiveData<ArrayList<Service>> updateUserServicesList(int userId, ArrayList<Service> services) {
        serviceRepository = ServiceRepository.getInstance();
        mAllServicesList = serviceRepository.updateUserServicesList(userId,services);
        return mAllServicesList;
    }


    public LiveData<ResponseBody> updateUser(User user) {
        userRepository = userRepository.getInstance();
        responseBody = userRepository.updateUser(user);
        return responseBody;
    }

    public LiveData<ResponseBody> updatePassword(int userId, String oldPassword, String newPassword) {
        userRepository = userRepository.getInstance();
        responseBody = userRepository.updatePassword(userId,oldPassword,newPassword);
        return responseBody;
    }

    public LiveData<ResponseBody> setUserVisibilityInSearch(int userId, int visibility) {
        userRepository = userRepository.getInstance();
        responseBody = userRepository.setUserVisibilityInSearch( userId, visibility);
        return responseBody;
    }

    public LiveData<ErrorResponse> getUserRepoToast() {
        userRepository = UserRepository.getInstance();
        errorResponse = userRepository.getToast();
        return errorResponse;

    }
    public LiveData<ErrorResponse> getServiceRepoToast() {
        serviceRepository = ServiceRepository.getInstance();
        errorResponse = serviceRepository.getToast();
        return errorResponse;
    }


}