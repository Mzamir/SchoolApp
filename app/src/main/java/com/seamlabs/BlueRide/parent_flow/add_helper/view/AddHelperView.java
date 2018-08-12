package com.seamlabs.BlueRide.parent_flow.add_helper.view;

public interface AddHelperView {

    void showProgress();

    void hideProgress();

    void onErrorAddingHelper(String errorMessage) ;

    void onSuccessAddingHelper();

}
