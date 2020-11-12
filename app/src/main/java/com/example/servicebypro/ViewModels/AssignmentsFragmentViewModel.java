package com.example.servicebypro.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.Work;
import com.example.servicebypro.Repositories.ReviewRepository;
import com.example.servicebypro.Repositories.WorkRepository;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class AssignmentsFragmentViewModel extends AndroidViewModel {

    private static final String TAG = "WorkViewModel";
    public MutableLiveData<ArrayList<Work>> mWorkList;
    public MutableLiveData<ArrayList<Review>> mReviewsList;
    public MutableLiveData<ErrorResponse> errorResponse;
    private WorkRepository workRepository;
    private ReviewRepository reviewRepository;
    public MutableLiveData<ResponseBody> responseBody = new MutableLiveData<>();

    public AssignmentsFragmentViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG, "Work view model constructor");
    }

    public LiveData<ArrayList<Work>> getWorkList(String role, int idUser) {
        workRepository = WorkRepository.getInstance();
        mWorkList = workRepository.getWorksList(role, idUser);
        return mWorkList;
    }

    public LiveData<ArrayList<Work>> getWorkList(String role, int idUser , String status) {
        workRepository = WorkRepository.getInstance();
        mWorkList = workRepository.getWorksList(role, idUser, status);
        return mWorkList;
    }

    public LiveData<ResponseBody> acceptWorkProposal(com.example.servicebypro.Remote.Models.Application application) {
        workRepository = WorkRepository.getInstance();
        responseBody = workRepository.acceptWorkProposal(application);
        return responseBody;
    }


    public LiveData<ResponseBody> declineWorkProposal(com.example.servicebypro.Remote.Models.Application application) {
        workRepository = WorkRepository.getInstance();
        responseBody = workRepository.declineWorkProposal(application);
        return responseBody;
    }

    public LiveData<ResponseBody> finishWork(com.example.servicebypro.Remote.Models.Application application) {
        workRepository = WorkRepository.getInstance();
        responseBody = workRepository.finishWork(application);
        return responseBody;
    }

    public LiveData<ResponseBody> deleteWork(int workId) {
        workRepository = WorkRepository.getInstance();
        responseBody = workRepository.deleteWork(workId);
        return responseBody;
    }

    public LiveData<ResponseBody> cancelWork(int workId) {
        workRepository = WorkRepository.getInstance();
        responseBody = workRepository.cancelWork(workId);
        return responseBody;
    }

    public LiveData<ResponseBody> deleteApplication(int applicationId) {
        workRepository = WorkRepository.getInstance();
        responseBody = workRepository.deleteApplication(applicationId);
        return responseBody;
    }

    public LiveData<ResponseBody> addReview(Review review) {
        reviewRepository = ReviewRepository.getInstance();
        responseBody = reviewRepository.addReview(review);
        return responseBody;
    }

    public LiveData<ArrayList<Review>> getUserAsArtisanReviews(int artisanId) {
        reviewRepository = ReviewRepository.getInstance();
        mReviewsList = reviewRepository.getUserAsArtisanReviews(artisanId);
        return mReviewsList;
    }

    public LiveData<ArrayList<Review>> getUserAsClientReviews(int clientId) {
        reviewRepository = ReviewRepository.getInstance();
        mReviewsList = reviewRepository.getUserAsClientReviews(clientId);
        return mReviewsList;
    }

    public LiveData<ArrayList<Review>> getWorkReviews(int workId) {
        reviewRepository = ReviewRepository.getInstance();
        mReviewsList = reviewRepository.getWorkReviews(workId);
        return mReviewsList;
    }

    public LiveData<ErrorResponse> getToast() {
        workRepository = WorkRepository.getInstance();
        errorResponse = workRepository.getToastWork();
        return errorResponse;
    }

    public LiveData<ErrorResponse> getToastReview() {
        reviewRepository = ReviewRepository.getInstance();
        errorResponse = reviewRepository.getToastReview();
        return errorResponse;
    }

}