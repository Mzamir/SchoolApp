package com.seamlabs.BlueRide.parent_flow.home.presenter;

import android.util.Log;

import com.google.android.gms.common.AccountPicker;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.network.response.SchoolsResponse;
import com.seamlabs.BlueRide.network.response.ParentStudentForASchoolResponse;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.Constants;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class ParentHomeInteractor {


    public interface OnGettingParentSchoolFinishedListener {
        void onSuccessGettingParentSchools(ArrayList<SchoolsResponse> schoolsResponse);

        void onSuccessGettingParentStudentsForASchool(ArrayList<ParentStudentForASchoolResponse> parentStudentForASchoolResponses);

        void onError(String error);

        void onSuccessGettingUserData();

    }

    public void getParentSchools(final OnGettingParentSchoolFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getParentSchools()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<SchoolsResponse>>() {
                    @Override
                    public void onSuccess(ArrayList<SchoolsResponse> parentSchoolsRespons) {
                        if (parentSchoolsRespons == null) {
                            listener.onError(GENERAL_ERROR);
                            return;
                        }
                        Log.i("ParentHomeInteractor", "onSuccessGettingParentSchools " + parentSchoolsRespons.size());
                        listener.onSuccessGettingParentSchools(parentSchoolsRespons);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ParentHomeInteractor", "Exception" + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }

    public void getParentStudentsForASchool(String schoolId, final OnGettingParentSchoolFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getParentStudentsForASchool(BASE_URL + "students/" + String.valueOf(schoolId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<ParentStudentForASchoolResponse>>() {
                    @Override
                    public void onSuccess(ArrayList<ParentStudentForASchoolResponse> parentSchoolsResponse) {
                        if (parentSchoolsResponse == null) {
                            listener.onError(GENERAL_ERROR);
                            return;
                        }
                        Log.i("ParentHomeInteractor", "onSuccessGettingParentSchools " + parentSchoolsResponse.size());
                        listener.onSuccessGettingParentStudentsForASchool(parentSchoolsResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ParentHomeInteractor", "Exception" + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }

    public void updateUserData(final OnGettingParentSchoolFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getUserData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<UserResponseModel>>() {
                    @Override
                    public void onSuccess(Response<UserResponseModel> response) {
                        if (response.isSuccessful()) {
                            Log.i("updateUserData", "isSuccessful ");
                            UserResponseModel userResponseModel = response.body();
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            return;
                        }
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            listener.onError(jObjError.getString("errors"));
                            Log.i("updateUserData", "error");
                        } catch (Exception e) {
                            Log.i("updateUserData", "onSuccessException " + e.getMessage().toString());
                            listener.onError(SERVER_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i("updateUserData", "Exception " + e.getMessage().toString());
                        listener.onError(Constants.SERVER_ERROR);
                    }
                });

    }
}
