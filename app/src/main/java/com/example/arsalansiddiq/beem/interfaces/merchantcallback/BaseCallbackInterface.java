package com.example.arsalansiddiq.beem.interfaces.merchantcallback;

import retrofit2.Response;

/**
 * Created by jellani on 11/24/2018.
 */

public interface BaseCallbackInterface<T> {

    void success(Response<T> response);
    void failure(String error);

}
