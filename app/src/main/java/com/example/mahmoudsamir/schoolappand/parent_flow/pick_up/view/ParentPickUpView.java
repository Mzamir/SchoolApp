package com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.view;

import com.example.mahmoudsamir.schoolappand.network.response.ParentArrivedResponseModel;

public interface ParentPickUpView {
    void showProgress();

    void hideProgress();

    void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel);

    void onError();
}
