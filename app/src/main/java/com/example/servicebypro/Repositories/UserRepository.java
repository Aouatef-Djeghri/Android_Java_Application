package com.example.servicebypro.Repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Activities.HelperClasses.ErrorUtils;
import com.example.servicebypro.Remote.Api.RetrofitClient;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Service;
import com.example.servicebypro.Remote.Models.User;


import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.http.Field;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private static UserRepository instance;

    MutableLiveData<ErrorResponse> toastTest = new MutableLiveData<>();


    public static UserRepository getInstance() {
        Log.e(TAG, "getInstance repo");
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public MutableLiveData<User> login(String email, String password) {

        MutableLiveData<User> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().login(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<User> observer = new SingleObserver<User>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(User user) {
                Log.e(TAG, "onNext repo");
                data.setValue(user);
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


    public MutableLiveData<ResponseBody> createUser(User user) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().createUser(user)
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


    public MutableLiveData<ResponseBody> updateUser(User user) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().updateUser(user)
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

    public MutableLiveData<ResponseBody> updatePassword(int userId, String oldPassword, String newPassword) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().updatePassword(userId,oldPassword,newPassword)
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


    public MutableLiveData<ArrayList<User>> listAllUsersByService(int serviceId, int clientId) {

        MutableLiveData<ArrayList<User>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().listUsersByService(serviceId, clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<User>> observer = new SingleObserver<ArrayList<User>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<User> users) {
                Log.e(TAG, "onNext repo");
                data.setValue(users);
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


    public MutableLiveData<ResponseBody> forgetPassword(String email) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().forgetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ResponseBody> observer = new SingleObserver<ResponseBody>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                Log.e(TAG, "onNext repo");
                data.setValue(responseBody);
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


    public MutableLiveData<ResponseBody> setUserVisibilityInSearch(int userId, int visibility) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().setUserVisibilityInSearch(userId,visibility)
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


    public MutableLiveData<ErrorResponse> getToast() {
        toastTest = new MutableLiveData<>();
        return toastTest;
    }


}
