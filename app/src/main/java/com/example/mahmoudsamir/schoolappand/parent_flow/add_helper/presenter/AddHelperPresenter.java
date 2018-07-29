package com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.presenter;

import com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.view.AddHelperView;

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
    public void onError() {
        if (view != null) {
            view.hideProgress();
            view.onErrorAddingHelper();
        }
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.onSuccessAddingHelper();
        }
    }
}
