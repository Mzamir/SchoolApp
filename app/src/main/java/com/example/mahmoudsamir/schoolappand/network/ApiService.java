package com.example.mahmoudsamir.schoolappand.network;

import com.example.mahmoudsamir.schoolappand.network.response.HelperSignupResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSignupResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("/signup_parent")
    Single<ParentSignupResponse> signUpParent(@Field("national_id") String national_id, @Field("password") String password);

    @FormUrlEncoded
    @POST("/add_helper")
    Single<BaseResponse> addHelper(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/signup_helper")
    Single<HelperSignupResponse> signupHelper(@Field("name") String name
            , @Field("email") String email
            , @Field("password") String password
            , @Field("national_id") String national_id
            , @Field("phone") String phone);


    @FormUrlEncoded
    @POST("/login")
    Single<BaseResponse> login(@Field("email") String email ,  @Field("password") String password);

    @FormUrlEncoded
    @POST("/verify_code")
    Single<BaseResponse> verifyCode(@Field("code") String code ,  @Field("national_id") String national_id);
}
