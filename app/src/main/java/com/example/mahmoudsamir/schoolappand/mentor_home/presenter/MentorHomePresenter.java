package com.example.mahmoudsamir.schoolappand.mentor_home.presenter;

import com.example.mahmoudsamir.schoolappand.mentor_home.model.MentorStudentModel;
import com.example.mahmoudsamir.schoolappand.mentor_home.view.MentorHomeViewCommunicator;
import com.example.mahmoudsamir.schoolappand.network.response.MentorDeliverStudentsResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.MentorQueueResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentStudentForASchoolResponse;
import com.example.mahmoudsamir.schoolappand.network.response.StudentResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.StudentModel;

import java.util.ArrayList;

public class MentorHomePresenter implements MentorHomeInteractor.onMentorHomeListener {

    MentorHomeViewCommunicator view;
    MentorHomeInteractor interactor;

    public MentorHomePresenter(MentorHomeViewCommunicator view, MentorHomeInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void getMentorStudent() {
        if (view != null)
            view.showProgress();
        interactor.getMentorQueue(this);
    }

    public void deliverStudents(ArrayList<Integer> studentIDs) {
        if (view != null)
            view.showProgress();
        interactor.deliverStudents(studentIDs, this);
    }

    @Override
    public void onSuccessGettingStudents(ArrayList<MentorQueueResponseModel> mentorQueueResponseModels) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessGettingStudents(convertStudentsResponseToStudentModel(mentorQueueResponseModels));
        }

    }

    @Override
    public void onErrorGettingStudents() {
        if (view != null) {
            view.hideProgress();
            view.onError();
        }
    }

    @Override
    public void onSuccessDeliverAction() {
        if (view != null) {
            view.hideProgress();
            view.onSuccessDeliverAction();
        }
    }

    @Override
    public void onErrorDeliverAction() {
        if (view != null) {
            view.hideProgress();
            view.onError();
        }
    }

    private ArrayList<MentorStudentModel> convertStudentsResponseToStudentModel(ArrayList<MentorQueueResponseModel> responseModels) {
        ArrayList<MentorStudentModel> studentModels = new ArrayList<>();
        for (MentorQueueResponseModel response : responseModels) {
            for (StudentResponseModel studentResponseModel : response.getStudents()) {
                MentorStudentModel studentModel = new MentorStudentModel();
                studentModel.setMarked(false);
                studentModel.setStudentID(studentResponseModel.getId());
                studentModel.setStudentName(studentResponseModel.getName());
                studentModel.setStudentNationalID(studentResponseModel.getNational_id());
                studentModel.setSchoolID(studentResponseModel.getSchool_id());
                studentModel.setClassID(studentResponseModel.getClass_id());
                studentModel.setStudentCreatedAt(studentResponseModel.getCreated_at());
                studentModel.setStudentUpdatedAt(studentResponseModel.getUpdated_at());
                studentModel.setRequestId(response.getId());
                studentModel.setRequestState(response.getStatus());
                studentModel.setStudentPicture(studentResponseModel.getImages().get(0).getPath());
                studentModels.add(studentModel);
            }
        }
        return studentModels;
    }
}
