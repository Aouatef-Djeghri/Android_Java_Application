package com.example.servicebypro.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Activities.HelperClasses.ErrorUtils;
import com.example.servicebypro.Remote.Api.RetrofitClient;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.UploadResponse;
import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class ImagesRepository {


    private static final String TAG = "ImageRepository";

    private static ImagesRepository instance;

    MutableLiveData<ErrorResponse> toast = new MutableLiveData<>();

    public static ImagesRepository getInstance() {
        if (instance == null) {
            instance = new ImagesRepository();
        }
        return instance;
    }

    public MutableLiveData<UploadResponse> uploadIdImage(MultipartBody.Part body,String dir, String imageName) {

        MutableLiveData<UploadResponse> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().uploadIdImage(body,dir,imageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<UploadResponse> observer = new SingleObserver<UploadResponse>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(UploadResponse responseBodyResponse) {
                Log.e(TAG, "onNext repo");
                data.setValue(responseBodyResponse);
            }
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError:  repo"+t.getMessage());
                ErrorResponse error;
                try {

                    Response body = ((HttpException) t).response();
                    error = ErrorUtils.parseError(body);
                    toast.setValue(error);

                    Log.e(TAG, "try method repo"+t.getMessage());
                } catch (Exception e) {
                    error = new ErrorResponse();
                    //todo  there is more than just a failing request (like: no internet connection)
                    if (t instanceof IOException) {
                        error.setErrorMessage("Server Error: No Internet Connection!");
                    }else
                    if (!(t instanceof HttpException)) {
                        error.setErrorMessage("Server Error: Please try again.\nReason : " + t.getMessage());
                    } else {
                        error.setErrorMessage("Server Error: unknown. Image didn't upload , Please try again ! " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toast.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }


    public MutableLiveData<UploadResponse> uploadWorkImages(MultipartBody.Part imageOneBody,
                                                         MultipartBody.Part imageTwoBody,
                                                         MultipartBody.Part imageThreeBody) {

        MutableLiveData<UploadResponse> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().uploadWorkImages(imageOneBody,imageTwoBody,imageThreeBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<UploadResponse> observer = new SingleObserver<UploadResponse>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(UploadResponse responseBodyResponse) {
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
                    toast.setValue(error);

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
                        error.setErrorMessage("Server Error: unknown. Image didn't upload , Please try again ! " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toast.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }



    public MutableLiveData<ResponseBody> downloadFileWithDynamicUrlAsync( String fileUrl) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().downloadFileWithDynamicUrlAsync(fileUrl)
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
                Log.e(TAG, "onError:  repo"+t.getMessage());
                ErrorResponse error;
                try {

                    Response body = ((HttpException) t).response();
                    error = ErrorUtils.parseError(body);
                    toast.setValue(error);

                    Log.e(TAG, "try method repo"+t.getMessage());
                } catch (Exception e) {
                    error = new ErrorResponse();
                    //todo  there is more than just a failing request (like: no internet connection)
                    if (t instanceof IOException) {
                        error.setErrorMessage("Server Error: No Internet Connection!");
                    }else
                    if (!(t instanceof HttpException)) {
                        error.setErrorMessage("Server Error: Please try again.\nReason : " + t.getMessage());
                    } else {
                        error.setErrorMessage("Server Error: unknown. Image didn't upload , Please try again ! " + t.getMessage());
                    }
                    error.setErrorCode(500);
                    error.setDocumentation("");
                    toast.setValue(error);
                    Log.e(TAG, "catch method repo");
                }
            }

        };

        observable.subscribe(observer);
        return data;
    }

    public MutableLiveData<ErrorResponse> getToast() {
        toast = new MutableLiveData<>();
        return toast;
    }


}
