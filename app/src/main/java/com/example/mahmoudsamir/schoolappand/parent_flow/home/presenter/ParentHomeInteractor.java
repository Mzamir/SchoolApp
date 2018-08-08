package com.example.mahmoudsamir.schoolappand.parent_flow.home.presenter;

import android.util.Log;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.requests.ParentPickUpRequestModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentPickUpResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.SchoolsResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentStudentForASchoolResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.BASE_URL;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.GENERAL_ERROR;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SERVER_ERROR;

public class ParentHomeInteractor {

    public interface OnGettingParentSchoolFinishedListener {
        void onSuccessGettingParentSchools(ArrayList<SchoolsResponse> schoolsResponse);

        void onSuccessGettingParentStudentsForASchool(ArrayList<ParentStudentForASchoolResponse> parentStudentForASchoolResponses);

        void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel);

        void onError(String error);
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
                        if (parentSchoolsRespons == null || parentSchoolsRespons.size() == 0) {
                            listener.onError("Unauthenticated");
                        } else {
                            Log.i("ParentHomeInteractor", "onSuccessGettingParentSchools " + parentSchoolsRespons.size());
                            listener.onSuccessGettingParentSchools(parentSchoolsRespons);
                        }
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
                        if (parentSchoolsResponse == null || parentSchoolsResponse.size() == 0) {
                            listener.onError("Unauthenticated");
                        } else {
                            Log.i("ParentHomeInteractor", "onSuccessGettingParentSchools " + parentSchoolsResponse.size());
                            listener.onSuccessGettingParentStudentsForASchool(parentSchoolsResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ParentHomeInteractor", "Exception" + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }

    public void parentPickUpRequest(ParentPickUpRequestModel requestModel, final OnGettingParentSchoolFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.parentPickUpRequest(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ParentPickUpResponseModel>() {
                    @Override
                    public void onSuccess(ParentPickUpResponseModel parentSchoolsResponse) {
                        if (parentSchoolsResponse == null) {
                            listener.onError(GENERAL_ERROR);
                        } else {
                            if (parentSchoolsResponse.getMessage() != null) {
                                listener.onError(GENERAL_ERROR);
                            } else {
                                listener.onSuccessPickUpRequest(parentSchoolsResponse);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ParentHomeInteractor", "Exception" + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }
}
