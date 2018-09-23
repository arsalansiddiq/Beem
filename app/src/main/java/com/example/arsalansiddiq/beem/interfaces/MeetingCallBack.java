package com.example.arsalansiddiq.beem.interfaces;

import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;

import retrofit2.Response;

/**
 * Created by jellani on 9/22/2018.
 */

public interface MeetingCallBack {

    void success(Response<MeetingResponseModel> responseModelResponse);
    void error(String error);
}
