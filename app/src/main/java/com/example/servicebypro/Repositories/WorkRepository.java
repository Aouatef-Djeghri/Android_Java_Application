package com.example.servicebypro.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Activities.HelperClasses.ErrorUtils;
import com.example.servicebypro.Remote.Api.RetrofitClient;
import com.example.servicebypro.Remote.Models.Application;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Review;
import com.example.servicebypro.Remote.Models.User;
import com.example.servicebypro.Remote.Models.Work;

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

public class WorkRepository {

    private static final String TAG = "WorkRepository";

    private static WorkRepository instance;

    MutableLiveData<ErrorResponse> toastTest = new MutableLiveData<>();

    public static WorkRepository getInstance() {
        Log.e(TAG, "getInstance repo");
        if (instance == null) {
            instance = new WorkRepository();
        }
        return instance;
    }

    //get data from a webservice
    public MutableLiveData<ArrayList<Work>> getWorksList(String role, int idUser) {

        MutableLiveData<ArrayList<Work>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getWorkListObservable(role, idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Work>> observer = new SingleObserver<ArrayList<Work>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Work> works) {
                Log.e(TAG, "onNext repo");
                data.setValue(works);
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
    public MutableLiveData<ArrayList<Work>> getWorksList(String role, int idUser , String status) {

        MutableLiveData<ArrayList<Work>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getWorkListObservable(role, idUser , status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Work>> observer = new SingleObserver<ArrayList<Work>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Work> works) {
                data.setValue(works);
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
    public MutableLiveData<ArrayList<Work>> getAllAvailableWorkList(int idUser) {

        MutableLiveData<ArrayList<Work>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getAllAvailableWorkList(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Work>> observer = new SingleObserver<ArrayList<Work>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Work> works) {
                Log.e(TAG, "onNext repo");
                data.setValue(works);
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
    public MutableLiveData<ArrayList<Work>> getRecommendedWorkList(int idUser) {

        MutableLiveData<ArrayList<Work>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getRecommendedWorkList(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Work>> observer = new SingleObserver<ArrayList<Work>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Work> works) {
                Log.e(TAG, "onNext repo");
                data.setValue(works);
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
    public MutableLiveData<ArrayList<Work>> getAllAvailableWorkListByCategory(int idCategory, int idUser) {

        MutableLiveData<ArrayList<Work>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getAllAvailableWorkListByCategory(idCategory,idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Work>> observer = new SingleObserver<ArrayList<Work>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Work> works) {
                Log.e(TAG, "onNext repo");
                data.setValue(works);
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

    public MutableLiveData<ArrayList<Work>> getAllAvailableWorkListByService(int idService, int idUser) {

        MutableLiveData<ArrayList<Work>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getAllAvailableWorkListByService(idService,idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Work>> observer = new SingleObserver<ArrayList<Work>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Work> works) {
                Log.e(TAG, "onNext repo");
                data.setValue(works);
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

    //add work
    public MutableLiveData<ResponseBody> createWorkPost(Work work) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().createWorkPost(work)
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

    public MutableLiveData<ResponseBody> createWorkProposal(com.example.servicebypro.Remote.Models.Application application) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().createWorkProposal(application)
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


    public MutableLiveData<ResponseBody> createApplication(com.example.servicebypro.Remote.Models.Application application) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().createApplication(application)
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


    public MutableLiveData<ResponseBody> acceptWorkProposal(com.example.servicebypro.Remote.Models.Application application) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().acceptWorkProposal(application)
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


    public MutableLiveData<ResponseBody> declineWorkProposal(com.example.servicebypro.Remote.Models.Application application) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().declineWorkProposal(application)
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


    public MutableLiveData<ResponseBody> finishWork(com.example.servicebypro.Remote.Models.Application application) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().finishWork(application)
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


    public MutableLiveData<ResponseBody> deleteWork(int workId) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().deleteWork(workId)
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


    public MutableLiveData<ResponseBody> cancelWork(int workId) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().cancelWork(workId)
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


    public MutableLiveData<ResponseBody> deleteApplication(int applicationId) {

        MutableLiveData<ResponseBody> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().deleteApplication(applicationId)
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

    public MutableLiveData<ErrorResponse> getToastWork() {
        toastTest = new MutableLiveData<>();
        return toastTest;
    }


}