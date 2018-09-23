package com.example.arsalansiddiq.beem.utils.data;

import retrofit2.Response;

/**
 * Created by jellani on 9/21/2018.
 */

public class UpdateRH<T> extends BaseRes<T> {

    private UpdateCallback updateCallback;

    public UpdateRH(UpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
    }

    @Override
    public void onSuccess(Response<T> response) {
        updateCallback.UpdateSuccess(response);
    }

    @Override
    public void onError(BaseResponse baseResponse) {
        updateCallback.UpdateFailure(baseResponse);
    }
}
