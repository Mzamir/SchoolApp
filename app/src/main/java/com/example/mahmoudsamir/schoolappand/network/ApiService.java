package com.example.mahmoudsamir.schoolappand.network;

import com.example.mahmoudsamir.schoolappand.network.requests.ParentPickUpRequestModel;
import com.example.mahmoudsamir.schoolappand.network.response.HelperSignupResponse;
import com.example.mahmoudsamir.schoolappand.network.response.LoginResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentArrivedResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentPickUpResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSignupResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSchoolsResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentStudentForASchoolResponse;


import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @POST("signup_parent")
    Single<ParentSignupResponse> signUpParent(@Field("national_id") String national_id, @Field("password") String password);

    @FormUrlEncoded
    @POST("add_helper")
    Single<BaseResponse> addHelper(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("signup_helper")
    Single<HelperSignupResponse> signupHelper(@Field("name") String name
            , @Field("email") String email
            , @Field("password") String password
            , @Field("national_id") String national_id
            , @Field("phone") String phone);


    @FormUrlEncoded
    @POST("login")
    Single<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("verify_code")
    Single<BaseResponse> verifyCode(@Field("code") String code, @Field("national_id") String national_id);

    @POST("resend_verification_code")
    Single<BaseResponse> resendVerificationCode(@Path("") String id);

    @GET("schools")
    Single<ArrayList<ParentSchoolsResponse>> getParentSchools();

    @GET
    Single<ArrayList<ParentStudentForASchoolResponse>> getParentStudentsForASchool(@Url() String schoolID);

    @POST("pick_request")
    Single<ParentPickUpResponseModel> parentPickUpRequest(@Body ParentPickUpRequestModel pickUpRequestModel);

    @FormUrlEncoded
    @PUT("parent_arrived")
    Single<ParentArrivedResponseModel> parentArrived(@Field("request_id") int request_id);

    @FormUrlEncoded
    @PUT("report")
    Single<BaseResponse> report(@Field("request_id") int request_id);

    @FormUrlEncoded
    @PUT("delivered")
    Single<BaseResponse> delivered(@Field("request_id") int request_id);

}
