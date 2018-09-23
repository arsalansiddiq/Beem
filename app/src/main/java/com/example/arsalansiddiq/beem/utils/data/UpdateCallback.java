package com.example.arsalansiddiq.beem.utils.data;

import retrofit2.Response;

/**
 * Created by jellani on 9/21/2018.
 */

public interface UpdateCallback<T>  {
    void UpdateSuccess(Response response);
    void UpdateFailure(BaseResponse baseResponse);
}
