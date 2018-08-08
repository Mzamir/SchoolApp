package com.example.mahmoudsamir.schoolappand.network;

import com.example.mahmoudsamir.schoolappand.network.requests.ParentPickUpRequestModel;
import com.example.mahmoudsamir.schoolappand.network.requests.UserRequestModel;
import com.example.mahmoudsamir.schoolappand.network.response.MentorDeliverStudentsResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.MentorQueueResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentArrivedResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentPickUpResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.SchoolsResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentStudentForASchoolResponse;
import com.example.mahmoudsamir.schoolappand.network.response.UserProfileResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.UserResponseModel;


import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @POST("signup_parent")
    Single<UserResponseModel> signUpParent(@Field("national_id") String national_id, @Field("password") String password);

    @FormUrlEncoded
    @POST("add_helper")
    Single<BaseResponse> addHelper(@Field("phone") String phone);

    @POST("signup_helper")
    Single<UserResponseModel> signupHelper(@Body UserRequestModel userRequestModel);


    @FormUrlEncoded
    @POST("login")
    Single<UserResponseModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("verify_code")
    Single<BaseResponse> verifyCode(@Field("code") String code, @Field("national_id") String national_id);

    @POST("resend_verification_code")
    Single<BaseResponse> resendVerificationCode(@Path("") String id);

    @GET("schools")
    Single<ArrayList<SchoolsResponse>> getParentSchools();

    // Get students for specific school
    @GET
    Single<ArrayList<ParentStudentForASchoolResponse>> getParentStudentsForASchool(@Url() String schoolID);

    @POST("pick_request")
    Single<ParentPickUpResponseModel> parentPickUpRequest(@Body ParentPickUpRequestModel pickUpRequestModel);

    @FormUrlEncoded
    @PUT("parent_arrived")
    Single<ParentArrivedResponseModel> parentArrived(@Field("request_id") int request_id);

    @FormUrlEncoded
    @PUT("report")
    Single<ParentArrivedResponseModel> report(@Field("request_id") int request_id);

    @FormUrlEncoded
    @PUT("delivered")
    Single<BaseResponse> delivered(@Field("request_id") int request_id);

    @FormUrlEncoded
    @PUT("mentor_deliver_students")
    Single<ArrayList<MentorDeliverStudentsResponseModel>> mentorDeliverStudentsAction(@Field("requests_id[]") ArrayList<Integer> students_ids);

    @GET("mentor_queue")
    Single<ArrayList<MentorQueueResponseModel>> getMentorQueue();

    @GET("user_profile")
    Single<UserProfileResponseModel> getUserProfile();
}
