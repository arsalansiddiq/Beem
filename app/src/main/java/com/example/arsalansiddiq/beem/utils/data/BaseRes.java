package com.example.arsalansiddiq.beem.utils.data;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jellani on 9/21/2018.
 */

public abstract class BaseRes<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response);
        } else {
            Log.i("LogBaseOnSucces", "Hi From Response" + response.code());
            onError(new BaseResponse(String.valueOf(response.code())));

        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t != null) {
            onError(new BaseResponse(t.getLocalizedMessage().toString()));
        } else {
            onError(new BaseResponse("something went wrong!"));
        }
    }

    public abstract void onSuccess(Response<T> response);
    public abstract void onError(BaseResponse baseResponse);
}
