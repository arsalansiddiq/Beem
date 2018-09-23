package com.example.arsalansiddiq.beem.activities.presenter;

import com.example.arsalansiddiq.beem.activities.contractor.NavigationDrawerContractorSUP;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import retrofit2.Response;

/**
 * Created by jellani on 9/20/2018.
 */

public class NavigationDrawerSUPPresenter implements NavigationDrawerContractorSUP.NavigationDrawerPresenter, UpdateCallback {

    private RealmCRUD realmCRUD;
    private NetworkUtils networkUtils;
    private NavigationDrawerContractorSUP.NavigationView navigationView;

    private String state = null;

    public NavigationDrawerSUPPresenter(NetworkUtils networkUtils, NavigationDrawerContractorSUP.NavigationView navigationView) {
        this.networkUtils = networkUtils;
        this.navigationView = navigationView;

        realmCRUD = new RealmCRUD();
    }

    @Override
    public void onDestroy() {
        navigationView = null;
    }

    @Override
    public void onClickButtonStartMeeting(String state, StartMeetingRequest startMeetingRequest) {
        this.state = state;
//        if (state.equals("StartMeeting")) {
//            networkUtils.startMeeting(startMeetingRequest, this);
//        }
    }

    @Override
    public void getTaskOnActivityLaunch(String state, int empd) {
        this.state = state;
        if (realmCRUD.getAllTasks() != null) {
            navigationView.showTaskList();
        } else {
            if (networkUtils.isNetworkConnected()) {
                navigationView.showProgress();
                networkUtils.getTaskList(empd, this);
            } else {
                navigationView.showError("please check you internet connection");
            }
        }
    }

    @Override
    public void updateMeeting(String state, StartMeetingRequest startMeetingRequest) {
        this.state = state;
            if (networkUtils.isNetworkConnected()) {
                navigationView.showProgress();
//                networkUtils.updateMeeting(startMeetingRequest, this);
            } else {
                navigationView.showError("please check you internet connection");
            }
    }

    @Override
    public void UpdateSuccess(Response response) {
        navigationView.hideProgress();
        if (response != null) {
            if (response.isSuccessful()) {
                if (state.equals("StartMeeting")) {
                    navigationView.showSuccesofState_startMeeting(response, state);
                } else if (state.equals("GetTask")) {
                    navigationView.showSuccesofState_getTaskList(response);
                } else if (state.equals("UpdateMeeting")) {
                    navigationView.showSuccesofState_startMeeting(response, state);
                }
            }
        }
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        navigationView.hideProgress();
        navigationView.showError(baseResponse.getError());
    }
}

