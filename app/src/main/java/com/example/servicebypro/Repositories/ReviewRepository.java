package com.example.servicebypro.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Activities.HelperClasses.ErrorUtils;
import com.example.servicebypro.Remote.Api.RetrofitClient;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;


import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class ReviewRepository {

    private static final String TAG = "ReviewRepository";

    private static ReviewRepository instance;

    MutableLiveData<ErrorResponse> toastTest = new MutableLiveData<>();

    public static ReviewRepository getInstance() {
        Log.e(TAG, "getInstance repo");
        if (instance == null) {
            instance = new ReviewRepository();
        }
        return instance;
    }


    public MutableLiveData<ResponseBody> addReview(Review review) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().addReview(review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ResponseBody> observer = new SingleObserver<ResponseBody>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ResponseBody responseBodyResponse) {
                Log.e(TAG, "onNext repo");
                data.setValue(responseBodyResponse);
            }
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError:  repo");
                ErrorResponse error;
                try {
                    Response body = ((HttpException) t).response();
                    error = ErrorUtils.parseError(body);
                    toastTest.setValue(error);

                    Log.e(TAG, "try method repo");
                } catch (Exception e) {
                    error = new ErrorResponse();
                    //todo  there is more than just a failing request (like: no internet connection)
                    if (t instanceof IOException) {
                        error.setErrorMessage("Server Error: No Internet Connection!");
                    }else
                    if (!(t instanceof HttpException)) {
                        error.setErrorMessage("Server Error: Please try again.\nReason : " + t.getMessage());
                    } else {
                        error.setErrorMessage("Server Error: unknown. " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toastTest.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }


    //get data from a webservice
    public MutableLiveData<ArrayList<Review>> getUserAsArtisanReviews(int artisanId) {

        MutableLiveData<ArrayList<Review>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getUserAsArtisanReviews(artisanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Review>> observer = new SingleObserver<ArrayList<Review>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Review> reviews) {
                Log.e(TAG, "onNext repo");
                data.setValue(reviews);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError:  repo");
                ErrorResponse error;
                try {
                    Response body = ((HttpException) t).response();
                    error = ErrorUtils.parseError(body);
                    toastTest.setValue(error);

                    Log.e(TAG, "try method repo");
                } catch (Exception e) {
                    error = new ErrorResponse();
                    //todo  there is more than just a failing request (like: no internet connection)
                    if (t instanceof IOException) {
                        error.setErrorMessage("Server Error: No Internet Connection!");
                    }else
                    if (!(t instanceof HttpException)) {
                        error.setErrorMessage("Server Error: Please try again.\nReason : " + t.getMessage());
                    } else {
                        error.setErrorMessage("Server Error: unknown. " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toastTest.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }


    public MutableLiveData<ArrayList<Review>> getUserAsClientReviews(int clientId) {

        MutableLiveData<ArrayList<Review>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getUserAsClientReviews(clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Review>> observer = new SingleObserver<ArrayList<Review>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Review> reviews) {
                Log.e(TAG, "onNext repo");
                data.setValue(reviews);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError:  repo");
                ErrorResponse error;
                try {
                    Response body = ((HttpException) t).response();
                    error = ErrorUtils.parseError(body);
                    toastTest.setValue(error);

                    Log.e(TAG, "try method repo");
                } catch (Exception e) {
                    error = new ErrorResponse();
                    //todo  there is more than just a failing request (like: no internet connection)
                    if (t instanceof IOException) {
                        error.setErrorMessage("Server Error: No Internet Connection!");
                    }else
                    if (!(t instanceof HttpException)) {
                        error.setErrorMessage("Server Error: Please try again.\nReason : " + t.getMessage());
                    } else {
                        error.setErrorMessage("Server Error: unknown. " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toastTest.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }


    public MutableLiveData<ArrayList<Review>> getWorkReviews(int workId) {

        MutableLiveData<ArrayList<Review>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getWorkReviews(workId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Review>> observer = new SingleObserver<ArrayList<Review>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Review> reviews) {
                Log.e(TAG, "onNext repo");
                data.setValue(reviews);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError:  repo");
                ErrorResponse error;
                try {
                    Response body = ((HttpException) t).response();
                    error = ErrorUtils.parseError(body);
                    toastTest.setValue(error);

                    Log.e(TAG, "try method repo");
                } catch (Exception e) {
                    error = new ErrorResponse();
                    //todo  there is more than just a failing request (like: no internet connection)
                    if (t instanceof IOException) {
                        error.setErrorMessage("Server Error: No Internet Connection!");
                    }else
                    if (!(t instanceof HttpException)) {
                        error.setErrorMessage("Server Error: Please try again.\nReason : " + t.getMessage());
                    } else {
                        error.setErrorMessage("Server Error: unknown. " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toastTest.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }



    public MutableLiveData<ErrorResponse> getToastReview() {
        toastTest = new MutableLiveData<>();
        return toastTest;
    }


}