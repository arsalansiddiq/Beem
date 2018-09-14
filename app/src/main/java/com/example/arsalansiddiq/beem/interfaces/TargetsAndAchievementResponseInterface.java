package com.example.arsalansiddiq.beem.interfaces;


import com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel.TargetsandAchievementsModel;

import retrofit2.Response;

/**
 * Created by jellani on 9/4/2018.
 */

public interface TargetsAndAchievementResponseInterface  {

    void success(Response<TargetsandAchievementsModel> targetsandAchievementsModelResponse);
    void failed(String error);
}
