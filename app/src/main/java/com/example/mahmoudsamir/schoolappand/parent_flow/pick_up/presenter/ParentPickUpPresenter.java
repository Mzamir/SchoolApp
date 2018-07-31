package com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.presenter;

import com.example.mahmoudsamir.schoolappand.network.response.ParentArrivedResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.view.ParentPickUpView;

public class ParentPickUpPresenter implements ParentPickUpInteractor.onPickerArrivedListener {

    ParentPickUpView view;
    ParentPickUpInteractor interactor;

    public ParentPickUpPresenter(ParentPickUpView view, ParentPickUpInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void parent_arrived(int request_id) {
        if (view != null)
            view.showProgress();

        interactor.parent_arrived(request_id, this);
    }

    @Override
    public void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel) {
        if (view != null) {
            view.hideProgress();
            view.onSuccess(parentArrivedResponseModel);
        }
    }

    @Override
    public void onError() {
        if (view != null) {
            view.hideProgress();
            view.onError();
        }
    }
}
