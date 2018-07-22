package com.example.mahmoudsamir.schoolappand.parent_home.view;

import com.example.mahmoudsamir.schoolappand.network.response.UserSchoolsResponse;
import com.example.mahmoudsamir.schoolappand.parent_home.model.SchoolModel;

import java.util.ArrayList;
import java.util.List;

public interface ParentHomeView {
    void showProgress();

    void hideProgress();

    void onErrorGettingSchools(String error);

    void onSuccessGettingSchool(ArrayList<SchoolModel> schoolList);
}
