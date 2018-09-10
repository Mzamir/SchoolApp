package com.seamlabs.BlueRide.network;

import com.seamlabs.BlueRide.network.requests.AssignStudentsToHelperRequestModel;
import com.seamlabs.BlueRide.network.requests.CancelPickUpRequestModel;
import com.seamlabs.BlueRide.network.requests.EditProfileRequestModel;
import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.requests.TeacherDeliverStudentsRequestModel;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.requests.UserRequestModel;
import com.seamlabs.BlueRide.network.response.CheckRequestStateResponseModel;
import com.seamlabs.BlueRide.network.response.HelperProfileResponseModel;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.MentorDeliverStudentsResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;
import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.network.response.SchoolsResponse;
import com.seamlabs.BlueRide.network.response.ParentStudentForASchoolResponse;
import com.seamlabs.BlueRide.network.response.UpdateLocationResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;


import java.util.ArrayList;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @POST("signup_parent")
    Single<Response<UserResponseModel>> signUpParent(@Field("national_id") String national_id, @Field("password") String password);

    @FormUrlEncoded
    @POST("add_helper")
    Single<Response<BaseResponse>> addHelper(@Field("phone") String phone);

    @POST("signup_helper")
    Single<UserResponseModel> signupHelper(@Body UserRequestModel userRequestModel);

    @FormUrlEncoded
    @POST("signup_helper")
    Single<Response<UserResponseModel>> signupHelper(@Field("name") String name,
                                                     @Field("email") String email,
                                                     @Field("password") String password,
                                                     @Field("national_id") String national_id,
                                                     @Field("phone") String phone);


    @FormUrlEncoded
    @POST("login")
    Single<UserResponseModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("verify_code")
    Single<BaseResponse> verifyCode(@Field("code") String code, @Field("national_id") String national_id);


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

    @FormUrlEncoded
    @PUT("teacher_deliver_students")
    Single<MentorQueueResponseModel> teacherDeliverStudentsAction(
            @Field("request_ids[]") ArrayList<Integer> request_ids,
            @Field("student_ids[]") ArrayList<Integer> student_ids
    );


    @GET("mentor_queue")
    Single<ArrayList<MentorQueueResponseModel>> getMentorQueue();

    @GET("teacher_queue")
    Single<ArrayList<MentorQueueResponseModel>> getTeacherQueue();


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "cancel_request", hasBody = true)
//    @DELETE("cancel_request")
    Single<BaseResponse> cancelPickUpRequest(@Field("request_id") int request_id);

    @POST("/simulate")
    Single<String> sendCoordinates(@Body RequestBody coordinates);

    //    @FormUrlEncoded
    @POST("update_location")
    Single<Response<BaseResponse>> updateLocation(@Body UpdateLocationRequestModel locationRequestModel);

    //    @FormUrlEncoded
    @POST("update_location")
    Call<Response<BaseResponse>> updateLocationFromService(@Body UpdateLocationRequestModel updateLocationRequestModel);

    @GET("get_helpers")
    Single<ArrayList<HelperResponseModel>> getParentHelpers();

    @GET("user_profile")
    Single<UserProfileResponseModel> getUserProfile();

    @POST("user_profile")
    Single<Response<UserResponseModel>> editProfile(@Body EditProfileRequestModel editProfileRequestModel);


    @Multipart
    @POST("user_profile")
    Single<UserResponseModel> editProfile(@Part MultipartBody.Part image, @Part EditProfileRequestModel editProfileRequestModel);

    @Multipart
    @POST("user_profile")
    Single<UserResponseModel> editProfile(@Part MultipartBody.Part image);

    @Multipart
    @POST("student_image")
    Single<UserResponseModel> editStudentImage(@Part MultipartBody.Part image, @Part("national_id") RequestBody id);

    @Multipart
    @POST("user_profile")
    Single<UserResponseModel> editProfile(
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("current_password") RequestBody current_password,
            @Part("new_password") RequestBody new_password,
            @Part("confirm_password") RequestBody confirm_password,
            @Part("address") RequestBody address,
            @Part MultipartBody.Part image);

    @Multipart
    @POST("user_profile")
    Single<UserResponseModel> editProfile(
            @Part("address") RequestBody address);


    @POST("assign_students_to_helper")
    Single<BaseResponse> assignStudentsToHelper(@Body AssignStudentsToHelperRequestModel requestModel);

    @FormUrlEncoded
    @PUT("change_helper_status")
    Single<BaseResponse> changeHelperState(@Field("helper_id") int helper_id);

    @GET
    Single<HelperProfileResponseModel> getHelperProfile(@Url() String helper_id);

    @GET
    Single<BaseResponse> resendVerificationCode(@Url() String id);

    @GET
    Single<CheckRequestStateResponseModel> checkIfCanReceive(@Url() String id);

    @GET("notifications")
    Single<Response<ArrayList<NotificationResponseModel>>> getNotification();
}
