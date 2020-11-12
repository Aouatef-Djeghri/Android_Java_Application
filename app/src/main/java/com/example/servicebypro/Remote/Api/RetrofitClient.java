package com.example.servicebypro.Remote.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //Singleton class , when the app is using too much networking request using retrofit, defining a singleton class is a good idea

    private static final String BASE_URL = "http://localhost:8080/ServiceProRestfulApi/webapi/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(130, TimeUnit.SECONDS)
                .readTimeout(130, TimeUnit.SECONDS)
                .writeTimeout(130, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public  static  synchronized  RetrofitClient getInstance() {
        //synchronized a single instance only for each request
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public  Api getApi(){
        return  retrofit.create(Api.class);
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}
