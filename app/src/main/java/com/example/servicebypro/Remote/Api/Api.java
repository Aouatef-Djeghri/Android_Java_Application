package com.example.servicebypro.Remote.Api;

import com.example.servicebypro.Remote.Models.*;

import io.reactivex.Observable;

import java.util.ArrayList;

import io.reactivex.Single;
import okhttp3.MultipartBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

public interface Api {

    //interface for defining all the api calls

    //User Repo calls
    @FormUrlEncoded
    @POST("login")
    Single<User> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("login/forgetPassword")
    Single<ResponseBody> forgetPassword(@Field("email") String email);

    @POST("users/createUser")
    Single<ResponseBody> createUser(@Body User user);

    @POST("users/updateUser")
    Single<ResponseBody> updateUser(@Body User user);

    @FormUrlEncoded
    @POST("users/updatePassword")
    Single<ResponseBody> updatePassword(@Field("userId") int userId, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    //Work Repo calls
    @GET("works/{role}/{userId}")
    Call<ArrayList<Work>> getWorkListCallBack(@Path("role") String role, @Path("userId") int userId);

    @GET("works/{role}/{userId}")
    Single<ArrayList<Work>> getWorkListObservable(@Path("role") String role, @Path("userId") int userId);

    @GET("works/{role}/{userId}/{status}")
    Single<ArrayList<Work>> getWorkListObservable(@Path("role") String role, @Path("userId") int userId, @Path("status") String status);

    @POST("works/createWorkPost")
    Single<ResponseBody> createWorkPost(@Body Work work);

    @POST("works/createWorkProposal")
    Single<ResponseBody> createWorkProposal(@Body Application application);

    @POST("applications/createApplication")
    Single<ResponseBody> createApplication(@Body Application application);

    @POST("works/acceptWorkProposal")
    Single<ResponseBody> acceptWorkProposal(@Body Application application);

    @POST("works/declineWorkProposal")
    Single<ResponseBody> declineWorkProposal(@Body Application application);

    @POST("works/finishWork")
    Single<ResponseBody> finishWork(@Body Application application);

    @GET("works/posts/{userId}")
    Single<ArrayList<Work>> getAllAvailableWorkList(@Path("userId") int userId);

    @GET("works/posts/recommendation/user/{userId}")
    Single<ArrayList<Work>> getRecommendedWorkList(@Path("userId") int userId);

    @GET("works/posts/recommendation/category/{categoryId}/{userId}")
    Single<ArrayList<Work>> getAllAvailableWorkListByCategory(@Path("categoryId") int categoryId, @Path("userId") int userId);

    @GET("works/posts/recommendation/service/{serviceId}/{userId}")
    Single<ArrayList<Work>> getAllAvailableWorkListByService(@Path("serviceId") int serviceId, @Path("userId") int userId);

    //Service Repo calls
    @GET("categories/available")
    Single<ArrayList<Categorie>> getCategoriesList();

    @GET("services/available")
    Single<ArrayList<Service>> getAllServicesList();

    @GET("/categories/images")
    Observable<ResponseBody> getCategoriesImagesList();

    @GET("categories/{categoryId}/availableServices")
    Single<ArrayList<Service>> getServicesListByCategory(@Path("categoryId") int categoryId);

    @GET("services/user/{userId}")
    Single<ArrayList<Service>> getServicesArtisan(@Path("userId") int userId);

    @PUT("services/user/{userId}/update")
    Single<ArrayList<Service>> updateUserServicesList(@Path("userId") int userId, @Body
            ArrayList<Service> services);

    @POST("categories/create")
    public Call<Categorie> addCategorie(@Body Categorie categorie);

    //Using this you can upload file + others
    @Multipart
    @POST("upload/image/{dir}/{imageId}")
    Single<UploadResponse> uploadIdImage(@Part MultipartBody.Part file, @Path("dir") String dir,@Path("imageId") String imageId);

    @Multipart
    @POST("upload/workImages")
    Single<UploadResponse> uploadWorkImages(@Part MultipartBody.Part firstImage,
                                            @Part MultipartBody.Part secondImage,
                                            @Part MultipartBody.Part thirdImage);

    //Using this you can delete a file with a specific path
    @FormUrlEncoded
    @POST("upload/delete")
    public Call<ResponseBody> deleteUpload(@Field("imagePath") String imagePath);

    @FormUrlEncoded
    @POST("users/findArtisans")
    Single<ArrayList<User>> listUsersByService(@Field("serviceId") int serviceId, @Field("clientId") int clientId);

    @Streaming
    @GET
    Single<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

    @POST("reviews/create")
    Single<ResponseBody> addReview(@Body Review review);

    @GET("reviews/user/{artisanId}/artisan/reviewee")
    Single<ArrayList<Review>> getUserAsArtisanReviews(@Path("artisanId") int artisanId);

    @GET("reviews/user/{clientId}/client/reviewee")
    Single<ArrayList<Review>> getUserAsClientReviews(@Path("clientId") int clientId);

    @GET("reviews/work/{workId}")
    Single<ArrayList<Review>> getWorkReviews(@Path("workId") int workId);

    @DELETE("works/deleteWork/{workId}")
    Single<ResponseBody> deleteWork(@Path("workId") int workId);

    @PUT("works/cancelWork/{workId}")
    Single<ResponseBody> cancelWork(@Path("workId") int workId);

    @DELETE("applications/deleteApplication/{applicationId}")
    Single<ResponseBody> deleteApplication(@Path("applicationId") int applicationId);

    @PUT("users/{userId}/visibility/{visibility}")
    Single<ResponseBody> setUserVisibilityInSearch(@Path("userId") int userId,@Path("visibility") int visibility);

}
