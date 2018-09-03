package com.seamlabs.BlueRide.mentor_home.presenter;

import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;
import com.seamlabs.BlueRide.mentor_home.view.MentorHomeViewCommunicator;
import com.seamlabs.BlueRide.network.requests.TeacherDeliverStudentsRequestModel;
import com.seamlabs.BlueRide.network.response.MentorDeliverStudentsResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;
import com.seamlabs.BlueRide.network.response.ParentStudentForASchoolResponse;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;

import java.util.ArrayList;

import static com.seamlabs.BlueRide.utils.Constants.DELIVERD_TO_SUPERVISON;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_ARRIVED_STATE;
import static com.seamlabs.BlueRide.utils.Constants.PENDING_STATE;
import static com.seamlabs.BlueRide.utils.Constants.REPORTED_STATE;

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

    public void getTeacherStudents() {
        if (view != null)
            view.showProgress();
        interactor.getTeacherQueue(this);
    }

    public void deliverStudents(ArrayList<Integer> studentIDs) {
        if (view != null)
            view.showProgress();
        interactor.deliverStudents(studentIDs, this);
    }

    public void teachDeliverStudents(TeacherDeliverStudentsRequestModel requestModels) {
        if (view != null)
            view.showProgress();
        interactor.teacherDeliverStudents(requestModels, this);
    }

    @Override
    public void onSuccessGettingStudents(ArrayList<MentorQueueResponseModel> mentorQueueResponseModels) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessGettingStudents(convertStudentsResponseToStudentModel(mentorQueueResponseModels), mentorQueueResponseModels.size());
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

    public ArrayList<MentorStudentModel> convertStudentsResponseToStudentModel(ArrayList<MentorQueueResponseModel> responseModels) {
        ArrayList<MentorStudentModel> studentModels = new ArrayList<>();
        for (MentorQueueResponseModel response : responseModels) {
            for (StudentResponseModel studentResponseModel : response.getStudents()) {
                MentorStudentModel studentModel = new MentorStudentModel();
                studentModel.setMarked(false);
                studentModel.setMentorCanDeliver(response.isMentor_can_deliver());
                studentModel.setStudentID(studentResponseModel.getId());
                studentModel.setStudentName(studentResponseModel.getName());
                studentModel.setStudentNationalID(studentResponseModel.getNational_id());
                studentModel.setSchoolID(studentResponseModel.getSchool_id());
                studentModel.setClassID(studentResponseModel.getClass_id());
                studentModel.setClass_name(studentResponseModel.getClass_name());
                studentModel.setGrade_name(studentResponseModel.getGrade_name());
                studentModel.setStudentCreatedAt(studentResponseModel.getCreated_at());
                studentModel.setStudentUpdatedAt(studentResponseModel.getUpdated_at());

                studentModel.setRequestId(response.getId());
                if (response.getStatus().equals("pending"))
                    studentModel.setRequestState(PENDING_STATE);
                else if (response.getStatus().equals("parent_arrived")) {
                    studentModel.setRequestState(PARENT_ARRIVED_STATE);
                } else if (response.getStatus().equals("reported")) {
                    studentModel.setRequestState(REPORTED_STATE);
                } else if (response.getStatus().equals("delivered_to_supervisor")) {
                    studentModel.setRequestState(DELIVERD_TO_SUPERVISON);
                }
                studentModel.setStudentPicture(studentResponseModel.getImages().get(0).getPath());
                studentModels.add(studentModel);
            }
        }
        return sortStudentsBasedOnPriority(studentModels);
    }

    public ArrayList<MentorStudentModel> sortStudentsBasedOnPriority(ArrayList<MentorStudentModel> studentModels) {
        ArrayList<MentorStudentModel> sortedList = new ArrayList<>();
        ArrayList<MentorStudentModel> pendingList = new ArrayList<>();
        ArrayList<MentorStudentModel> reportedList = new ArrayList<>();
        ArrayList<MentorStudentModel> parentArrivedList = new ArrayList<>();

        for (MentorStudentModel studentModel : studentModels) {
            if (studentModel.getRequestState().equals(PENDING_STATE)) {
                pendingList.add(studentModel);
            } else if (studentModel.getRequestState().equals(PARENT_ARRIVED_STATE)) {
                parentArrivedList.add(studentModel);
            } else {
                reportedList.add(studentModel);
            }
        }
        sortedList.addAll(reportedList);
        sortedList.addAll(parentArrivedList);
        sortedList.addAll(pendingList);
        return sortedList;
    }
}
