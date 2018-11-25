package com.example.arsalansiddiq.beem.activities.contractor;

import android.util.Log;

import com.example.arsalansiddiq.beem.base.BaseViewPresenter;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.TaskResponse;

import retrofit2.Response;

/**
 * Created by jellani on 9/20/2018.
 */

public interface NavigationDrawerContractorSUP {

    interface NavigationDrawerPresenter extends BaseViewPresenter.BasePresenter {
        void onClickButtonStartMeeting(String state, StartMeetingRequest startMeetingRequest);
        void getTaskOnActivityLaunch(String state, int empd);
        void updateMeeting(String state, StartMeetingRequest startMeetingRequest);
    }

    interface NavigationView extends BaseViewPresenter.BaseView{

        default void defaultMethod () {
            Log.i("LoginContractor", "Default Method in Dumb LoginView!");
        }

        void showSuccesofState_getTaskList(Response<TaskResponse> taskResponseResponse);
        void showTaskList(TaskResponse taskResponse);
        void showSuccesofState_startMeeting(Response<MeetingResponseModel> taskResponseResponse, String state);
    }

}
