package com.seamlabs.BlueRide.notification.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class NotificationInteractor {
    final String TAG = NotificationInteractor.class.getSimpleName();

    public interface NotificationInteractorListener {
        void onErrorGettingNotifications(String errorMessage);

        void onSuccessGetttingNotifications(ArrayList<NotificationResponseModel> notificationsList);
    }

    public void getNotifications(final NotificationInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);


        apiService.getNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<ArrayList<NotificationResponseModel>>>() {
                    @Override
                    public void onSuccess(Response<ArrayList<NotificationResponseModel>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<NotificationResponseModel> notificationsList = response.body();
                            listener.onSuccessGetttingNotifications(notificationsList);
                            return;
                        }
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            listener.onErrorGettingNotifications(jObjError.getString("errors"));
                        } catch (Exception e) {
                            listener.onErrorGettingNotifications(SERVER_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "Exception " + e.getMessage().toString());
                        listener.onErrorGettingNotifications(Constants.SERVER_ERROR);
                    }
                });
    }
}
