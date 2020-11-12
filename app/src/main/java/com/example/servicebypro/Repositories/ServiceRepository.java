package com.example.servicebypro.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.servicebypro.Activities.HelperClasses.ErrorUtils;
import com.example.servicebypro.Remote.Api.RetrofitClient;
import com.example.servicebypro.Remote.Models.Categorie;
import com.example.servicebypro.Remote.Models.ErrorResponse;
import com.example.servicebypro.Remote.Models.Section;
import com.example.servicebypro.Remote.Models.Service;


import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

public class ServiceRepository {

    private static final String TAG = "ServiceRepository";

    private static ServiceRepository instance;

    MutableLiveData<ErrorResponse> toastTest = new MutableLiveData<>();


    public static ServiceRepository getInstance() {
        Log.e(TAG, "getInstance repo");
        if (instance == null) {
            instance = new ServiceRepository();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Categorie>> getCategoriesList() {

        MutableLiveData<ArrayList<Categorie>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getCategoriesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Categorie>> observer = new SingleObserver<ArrayList<Categorie>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Categorie> categories) {
                Log.e(TAG, "onNext repo");
                data.setValue(categories);
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
                    } else if (!(t instanceof HttpException)) {
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


    public MutableLiveData<ArrayList<Service>> getCategorieServicesList(int idCategory) {

        MutableLiveData<ArrayList<Service>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getServicesListByCategory(idCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Service>> observer = new SingleObserver<ArrayList<Service>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }

            @Override
            public void onSuccess(ArrayList<Service> services) {
                Log.e(TAG, "onNext repo");
                data.setValue(services);
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
                    } else if (!(t instanceof HttpException)) {
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


    public Section checkSection(ArrayList<Section> sectionList, String sectionName) {
        Section section = null;
        for (int j = 0; j < sectionList.size(); j++) {
            if (sectionList.get(j).getSectionName().equals(sectionName))
                section = sectionList.get(j);
        }
        return section;
    }

    public MutableLiveData<ArrayList<Section>> getServicesArtisanList(int idUser) {

        MutableLiveData<ArrayList<Section>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getServicesArtisan(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Service>> observer = new SingleObserver<ArrayList<Service>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }


            @Override
            public void onSuccess(ArrayList<Service> services) {

                ArrayList<Service> listFirstItem;
                ArrayList<Section> sectionList = new ArrayList<>();

                if (!services.isEmpty()) {

                    for (int i = 0; i < services.size(); i++) {
                        String key = services.get(i).getCategorie().getName();
                        System.out.println(" key : " + key);
                        Section checkSection = checkSection(sectionList, key);
                        System.out.println(" checkSection : " + checkSection);
                        if (checkSection != null) {
                            checkSection.getSectionItems().add(services.get(i));
                        } else {
                            ArrayList<Service> listRestOfItems = new ArrayList<Service>();
                            listRestOfItems.add(services.get(i));
                            sectionList.add(new Section(key, listRestOfItems));
                        }
                    }
                }
                data.setValue(sectionList);

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
                    } else if (!(t instanceof HttpException)) {
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

    //other version with Service not section
    public MutableLiveData<ArrayList<Service>> getServicesArtisanListNoSections(int idUser) {

        MutableLiveData<ArrayList<Service>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getServicesArtisan(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Service>> observer = new SingleObserver<ArrayList<Service>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }


            @Override
            public void onSuccess(ArrayList<Service> services) {
                data.setValue(services);

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
                    } else if (!(t instanceof HttpException)) {
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


    public MutableLiveData<ArrayList<Service>> getAllServicesList() {

        MutableLiveData<ArrayList<Service>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().getAllServicesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Service>> observer = new SingleObserver<ArrayList<Service>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }


            @Override
            public void onSuccess(ArrayList<Service> services) {
                data.setValue(services);
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
                    } else if (!(t instanceof HttpException)) {
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


    public MutableLiveData<ArrayList<Service>> updateUserServicesList(int userId, ArrayList<Service> services) {

        MutableLiveData<ArrayList<Service>> data = new MutableLiveData<>();

        Single observable = RetrofitClient.getInstance().getApi().updateUserServicesList(userId,services)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        SingleObserver<ArrayList<Service>> observer = new SingleObserver<ArrayList<Service>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe method repo");
            }


            @Override
            public void onSuccess(ArrayList<Service> services) {
                data.setValue(services);
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
                    } else if (!(t instanceof HttpException)) {
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
