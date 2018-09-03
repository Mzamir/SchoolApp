package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class ParentProfileInteactor {

    String TAG = ParentProfileInteactor.class.getSimpleName();

    public interface OnParentInteractorListener {
        void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel);

        void onErrorGettingUserProfile();

        void onSuccessEditingStudentImage();

        void onErrorEditingStudentImage(String message);

    }

    public void getUserProfile(final OnParentInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getUserProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserProfileResponseModel>() {
                    @Override
                    public void onSuccess(UserProfileResponseModel userProfileResponseModel) {
                        if (userProfileResponseModel.getMessage() == null) {
                            listener.onSuccessGettingUserProfile(userProfileResponseModel);
                        } else {
                            Log.i(TAG, "onSuccessParentArrived exception");
                            listener.onErrorGettingUserProfile();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingUserProfile();
                    }
                });
    }

    public void editStudentImage(String image, String national_id, final OnParentInteractorListener listener) {
        RequestBody national_id_body = RequestBody.create(MediaType.parse("text/plain"), national_id);
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        apiService.editStudentImage(getImageRequestPart(image), national_id_body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getErrors() != null) {
                                listener.onErrorEditingStudentImage(response.getErrors());
                                return;
                            }
                            if (response.getMessage() != null) {
                                listener.onErrorEditingStudentImage(response.getMessage());
                                return;
                            }
                            listener.onSuccessEditingStudentImage();
                        }
                        Log.i(TAG, "onSuccessParentArrived exception");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorEditingStudentImage(SERVER_ERROR);
                        e.printStackTrace();
                    }
                });
    }

    private MultipartBody.Part getImageRequestPart(String filePath) {
        if (!filePath.isEmpty()) {
            //pass it like this
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            return body;
        }
        return null;
    }
}
