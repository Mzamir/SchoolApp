package com.seamlabs.BlueRide.parent_flow.tracking_helper.presenter;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.parent_flow.profile.model.HelperModel;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class TrackingHelperInteractor {

    public interface TrackingHelperInteractorListener {
        void onSuccessGettingHelpers(ArrayList<HelperModel> helperModels);

        void onError(String errorMessage);
    }

    public void getHelpers(final TrackingHelperInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getParentHelpers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<ArrayList<HelperResponseModel>>() {
                    @Override
                    public void onSuccess(ArrayList<HelperResponseModel> helperResponseModels) {
                        if (helperResponseModels != null) {
                            listener.onSuccessGettingHelpers(convertResponseToModel(helperResponseModels));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
                    }
                });
    }

    private ArrayList<HelperModel> convertResponseToModel(ArrayList<HelperResponseModel> helperResponseModels) {
        ArrayList<HelperModel> helperModels = new ArrayList<>();

        for (HelperResponseModel responseModel : helperResponseModels) {
            HelperModel helperModel = new HelperModel();
            helperModel.setId(responseModel.getId());
            helperModel.setName(responseModel.getName());
            helperModel.setCreated_at(responseModel.getCreated_at());
            helperModel.setUpdated_at(responseModel.getUpdated_at());
            helperModel.setEmail(responseModel.getEmail());
            helperModel.setMarked(false);
            helperModel.setNational_id(responseModel.getNational_id());
            helperModel.setStatus(responseModel.getStatus());
            helperModel.setPhone(responseModel.getPhone());

            helperModels.add(helperModel);
        }
        return helperModels;
    }

}
