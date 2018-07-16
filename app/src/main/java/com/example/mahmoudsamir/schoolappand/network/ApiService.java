package com.example.mahmoudsamir.schoolappand.network;

import com.example.mahmoudsamir.schoolappand.network.requests.ParentSignInRequest;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSignInResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @FormUrlEncoded
    @POST("/signup_parent")
    Single<ParentSignInResponse> signUpParent(@Field("national_id") String national_id, @Field("password") String password);


//    @POST("/signup_parent")
//    void signUpParent(@Body ParentSignInRequest parentSignInRequest, Callback<ParentSignInResponse> parentSignInResponseCallback);
}
