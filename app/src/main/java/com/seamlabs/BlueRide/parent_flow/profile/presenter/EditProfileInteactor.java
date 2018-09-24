package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.seamlabs.BlueRide.utils.MessageEvent;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.requests.EditProfileRequestModel;
import com.seamlabs.BlueRide.network.response.ErrorResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.ADMIN_LOGIN_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.EVENT_PICTURE_CHANGED;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class EditProfileInteactor {
    String TAG = EditProfileInteactor.class.getSimpleName();

    public interface OnEditProfileInteractorListener {
        void onSuccessEditProfile(UserResponseModel userResponseModel);

        void onErrorGettingEditProfile(String errorMessage);

    }

    public void editProfile(EditProfileRequestModel editProfileRequestModel, final OnEditProfileInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        Log.i(TAG, "editProfile request " + new Gson().toJson(editProfileRequestModel));
        apiService.editProfile(editProfileRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {

                        if (response.isSuccessful()) {
                            UserResponseModel responseModel = (UserResponseModel) response.body();
                            if (responseModel.getLogin_as() != null) {
                                if (responseModel.getLogin_as().equalsIgnoreCase("admin") || responseModel.getLogin_as().equalsIgnoreCase("security")) {
                                    listener.onErrorGettingEditProfile(ADMIN_LOGIN_ERROR);
                                    return;
                                }
                            }
                            if (responseModel.getErrors() != null) {
                                listener.onErrorGettingEditProfile(responseModel.getErrors());
                                Log.i(TAG, "Error " + responseModel.getErrors());
                                return;
                            }
                            if (responseModel != null) {
                                if (responseModel.getEmail() != null) {
//                                    PrefUtils.storeApiKey(getMyApplicationContext(), responseModel.getToken());
//                                UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                                    UserSettingsPreference.saveUserProfile(getMyApplicationContext(), responseModel);
                                    listener.onSuccessEditProfile(responseModel);
                                } else {
                                    listener.onErrorGettingEditProfile(GENERAL_ERROR);
                                    Log.i(TAG, "Error " + responseModel.getErrors());
                                }
                            }
                        } else if (response.code() == 400) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("errors") != null) {
                                    if (!jObjError.getString("errors").isEmpty())
                                        listener.onErrorGettingEditProfile(jObjError.getString("errors"));
                                } else if (jObjError.getString("message") != null) {
                                    if (!jObjError.getString("message").isEmpty())
                                        listener.onErrorGettingEditProfile(jObjError.getString("message"));
                                }
                            } catch (Exception e) {
                                listener.onErrorGettingEditProfile(SERVER_ERROR);
                            }
                        } else if (response.code() == 422) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson = new Gson();
                                ErrorResponseModel responseModel = gson.fromJson(jObjError.getString("errors"), ErrorResponseModel.class);
                                if (responseModel.getEmail().size() > 0) {
                                    listener.onErrorGettingEditProfile(responseModel.getEmail().get(0));
                                } else if (responseModel.getNational_id().size() > 0) {
                                    listener.onErrorGettingEditProfile(responseModel.getNational_id().get(0));
                                } else if (responseModel.getPassword().size() > 0) {
                                    listener.onErrorGettingEditProfile(responseModel.getPassword().get(0));
                                } else if (responseModel.getPhone().size() > 0) {
                                    listener.onErrorGettingEditProfile(responseModel.getPhone().get(0));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onErrorGettingEditProfile(SERVER_ERROR);
                            }

                        } else {
                            listener.onErrorGettingEditProfile(SERVER_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingEditProfile(SERVER_ERROR);
                    }
                });
    }

    public void editProfile(String image, final boolean showProgress, final OnEditProfileInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        apiService.editProfile(getImageRequestPart(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel userResponseModel) {
                        if (userResponseModel != null) {
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            EventBus.getDefault().post(new MessageEvent(EVENT_PICTURE_CHANGED));
                            if (showProgress) {
                                listener.onSuccessEditProfile(userResponseModel);
                            }
                            return;
                        }
                        listener.onErrorGettingEditProfile(userResponseModel.getErrors());
                        Log.i(TAG, "onSuccessParentArrived exception");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingEditProfile(SERVER_ERROR);
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
