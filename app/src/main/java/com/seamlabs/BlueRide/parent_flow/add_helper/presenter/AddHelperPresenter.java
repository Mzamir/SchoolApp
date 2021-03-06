package com.seamlabs.BlueRide.parent_flow.add_helper.presenter;

import com.seamlabs.BlueRide.parent_flow.add_helper.view.AddHelperView;

public class AddHelperPresenter implements AddHelperInteractor.OnAddingHelperListener {

    AddHelperView view;
    AddHelperInteractor interactor;

    public AddHelperPresenter(AddHelperView view, AddHelperInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void addHelper(String phone) {
        if (view != null)
            view.showProgress();

        interactor.addHelper(phone, this);
    }

    @Override
    public void onError(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorAddingHelper(errorMessage);
        }
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.onSuccessAddingHelper();
        }
    }
}
