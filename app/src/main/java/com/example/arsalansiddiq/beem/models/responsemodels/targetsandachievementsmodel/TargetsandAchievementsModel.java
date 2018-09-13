package com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jellani on 9/4/2018.
 */

public class TargetsandAchievementsModel {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("Target")
        @Expose
        private List<Target> target = null;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<Target> getTarget() {
            return target;
        }

        public void setTarget(List<Target> target) {
            this.target = target;
        }

}
