package com.seamlabs.BlueRide.mentor_home.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.MentorDeliverStudentsResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MentorHomeInteractor {

    String TAG = MentorHomeInteractor.class.getSimpleName();

    public interface onMentorHomeListener {
        void onSuccessGettingStudents(ArrayList<MentorQueueResponseModel> mentorQueueResponseModels);

        void onErrorGettingStudents();

        void onSuccessDeliverAction();

        void onErrorDeliverAction();

    }

    public void getMentorQueue(final onMentorHomeListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getMentorQueue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<MentorQueueResponseModel>>() {
                    @Override
                    public void onSuccess(ArrayList<MentorQueueResponseModel> mentorQueueResponseModels) {
                        if (mentorQueueResponseModels != null) {
                            if (mentorQueueResponseModels.size() > 0) {
                                listener.onSuccessGettingStudents(mentorQueueResponseModels);
                                return;
                            }
                        }
                        listener.onErrorGettingStudents();
                        Log.i(TAG, "onSuccess getMentorQueue Exception ");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError getMentorQueue Exception " + e.getMessage());
                        listener.onErrorGettingStudents();
                    }
                });
    }

    public void deliverStudents(ArrayList<Integer> studentsIDs, final onMentorHomeListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.mentorDeliverStudentsAction(studentsIDs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<MentorDeliverStudentsResponseModel>>() {
                    @Override
                    public void onSuccess(ArrayList<MentorDeliverStudentsResponseModel> mentorQueueResponseModels) {
                        if (mentorQueueResponseModels != null) {
//                            if (mentorQueueResponseModels.size() > 0) {
                            listener.onSuccessDeliverAction();
                            return;
//                            }
                        }
                        listener.onErrorDeliverAction();
                        Log.i(TAG, "onSuccess deliverStudents Exception ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError deliverStudents Exception " + e.getMessage());
                        listener.onErrorDeliverAction();
                    }
                });
    }
}