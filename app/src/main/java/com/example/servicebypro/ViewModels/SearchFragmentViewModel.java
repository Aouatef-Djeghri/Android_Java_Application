package com.example.servicebypro.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.Repositories.ImagesRepository;
import com.example.servicebypro.Repositories.ServiceRepository;
import com.example.servicebypro.Repositories.WorkRepository;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class SearchFragmentViewModel extends AndroidViewModel {

    private static final String TAG = "SearchViewModel";
    public MutableLiveData<ArrayList<Work>> mWorkList;
    public MutableLiveData<ArrayList<Service>> mServiceList;
    public MutableLiveData<ErrorResponse> errorResponse;
    private WorkRepository workRepository;
    private ServiceRepository serviceRepository;
    private ImagesRepository imagesRepository;
    public MutableLiveData<ResponseBody> responseBody;

    public SearchFragmentViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "Work view model constructor");
    }

    public LiveData<ArrayList<Work>> getRecommendedWorkList(int idUser) {
        workRepository = WorkRepository.getInstance();
        mWorkList = workRepository.getRecommendedWorkList(idUser);
        return mWorkList;
    }

    public LiveData<ArrayList<Work>> getAllAvailableWorkList(int idUser) {
        workRepository = WorkRepository.getInstance();
        mWorkList = workRepository.getAllAvailableWorkList(idUser);
        return mWorkList;
    }

    public LiveData<ArrayList<Work>> getAllAvailableWorkListByCategory(int idCategory, int idUser) {
        workRepository = WorkRepository.getInstance();
        mWorkList = workRepository.getAllAvailableWorkListByCategory(idCategory, idUser);
        return mWorkList;
    }

    public LiveData<ArrayList<Work>> getAllAvailableWorkListByService(int idService, int idUser) {
        workRepository = WorkRepository.getInstance();
        mWorkList = workRepository.getAllAvailableWorkListByService(idService, idUser);
        return mWorkList;
    }

    public LiveData<ArrayList<Service>> getAllServicesList() {
        serviceRepository = ServiceRepository.getInstance();
        mServiceList = serviceRepository.getAllServicesList();
        return mServiceList;
    }

    public LiveData<ResponseBody> downloadFileWithDynamicUrlAsync( String fileUrl) {
        imagesRepository = ImagesRepository.getInstance();
        responseBody = imagesRepository.downloadFileWithDynamicUrlAsync(fileUrl);
        return responseBody;
    }

    public LiveData<ResponseBody> createApplication(com.example.servicebypro.Remote.Models.Application application) {
        workRepository = workRepository.getInstance();
        responseBody = workRepository.createApplication(application);
        return responseBody;
    }

    public LiveData<ErrorResponse> getToastImage() {

        imagesRepository = ImagesRepository.getInstance();
        errorResponse = imagesRepository.getToast();
        return errorResponse;
    }


    public LiveData<ErrorResponse> getToast() {

        workRepository = WorkRepository.getInstance();
        errorResponse = workRepository.getToastWork();
        return errorResponse;
    }

    public LiveData<ErrorResponse> getToastService() {

        serviceRepository = ServiceRepository.getInstance();
        errorResponse = serviceRepository.getToast();
        return errorResponse;
    }
}