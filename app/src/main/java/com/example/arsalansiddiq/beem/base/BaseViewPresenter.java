package com.example.arsalansiddiq.beem.base;

/**
 * Created by jellani on 9/20/2018.
 */

public interface BaseViewPresenter {

    interface BasePresenter {
        void onDestroy();
    }

    interface BaseView {

        void showProgress();
        void hideProgress();

        void showError(String error);

    }
}
